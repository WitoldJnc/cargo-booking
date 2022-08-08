package com.cargo.booking.nsi.service;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import com.cargo.booking.nsi.dto.XlsxHeader;
import com.cargo.booking.nsi.exception.HandleXlsxInputStreamException;
import com.cargo.booking.nsi.exception.MultipartFileIsEmptyException;
import com.cargo.booking.nsi.exception.RequiredDictionaryFieldNotPresentException;
import com.cargo.booking.nsi.exception.XlsxToObjProcessException;
import com.cargo.booking.nsi.model.ColumnType;
import com.cargo.booking.nsi.repository.AirlineRepository;
import com.cargo.booking.nsi.repository.AirportRepository;
import com.cargo.booking.nsi.repository.CurrencyRepository;
import com.cargo.booking.nsi.repository.ImpRepository;
import com.cargo.booking.nsi.util.XlsxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cargo.booking.nsi.model.DictionaryType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@ComponentScan("com.cargo.booking.messages")
public class XlsxParser<D, E> {
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int HEADER_ROW_INDEX = 0;

    private final ImpRepository impRepository;
    private final CurrencyRepository currencyRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;

    public List<D> parse(MultipartFile multipartFile, Class<D> cls) throws XlsxToObjProcessException, HandleXlsxInputStreamException, MultipartFileIsEmptyException {
        if (multipartFile.isEmpty()) {
            throw new MultipartFileIsEmptyException();
        }
        List<D> out = new ArrayList<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HandleXlsxInputStreamException(e);
        }

        for (Sheet sheet : workbook) {
            List<XlsxHeader> xlsxHeaders = XlsxUtil.modelObjectToXLSXHeader(cls);
            try {
                for (Row row : sheet) {
                    if (row.getRowNum() > HEADER_ROW_INDEX) {
                        out.add(createRowObject(xlsxHeaders, row, cls));
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
                throw new XlsxToObjProcessException(e);
            }
        }
        return out;
    }

    public E convertXlsxDtoToEntity(D dto, E entity) throws XlsxToObjProcessException {
        List<XlsxHeader> xlsxHeaders = XlsxUtil.modelObjectToXLSXHeader(dto.getClass());

        for (XlsxHeader header : xlsxHeaders) {
            Optional<String> entityDictionaryField = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(field -> field.getName().equals(header.getFieldName()))
                    .map(Field::getName)
                    .findFirst();

            if (entityDictionaryField.isPresent()) {
                try {
                    Method dictFieldSetter = Arrays.stream(entity.getClass().getDeclaredMethods())
                            .filter(method -> XlsxUtil.isSetterMethod(entityDictionaryField.get(), method))
                            .findFirst().get();

                    Optional<Object> rawDtoFieldValue = Optional.ofNullable(Arrays.stream(dto.getClass().getDeclaredMethods())
                            .filter(method -> XlsxUtil.isGetterMethod(entityDictionaryField.get(), method))
                            .findFirst().get()
                            .invoke(dto));

                    if (rawDtoFieldValue.isPresent()) {
                        String rawValue = rawDtoFieldValue.get().toString().trim();
                        Object value = switch (header.getColumnType()) {
                            case DATE -> parseDateFromInstantString(rawValue);
                            case INTEGER -> Integer.valueOf(rawValue);
                            case LINK -> findLinkRelation(rawValue, header);
                            case STRING -> rawValue;
                        };

                        dictFieldSetter.invoke(entity, value);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                    throw new XlsxToObjProcessException(e);
                }
            }
        }
        return entity;
    }

    private D createRowObject(List<XlsxHeader> xlsxHeaders, Row row, Class<D> dtoClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        D obj = dtoClass.newInstance();
        Method[] declaredMethods = obj.getClass()
                .getDeclaredMethods();

        for (XlsxHeader xlsxHeader : xlsxHeaders) {
            Cell cell = row.getCell(xlsxHeader.getXlsxColumnIndex());
            String field = xlsxHeader.getFieldName();
            ColumnType columnType = xlsxHeader.getColumnType();
            Method setter = Arrays.stream(declaredMethods)
                    .filter(method -> XlsxUtil.isSetterMethod(field, method))
                    .findFirst().get();

            if (xlsxHeader.getIsRequired() && cell == null) {
                throw new RequiredDictionaryFieldNotPresentException(field, row.getRowNum() + 1);
            } else {
                if (cell != null) {
                    switch (columnType) {
                        case INTEGER -> {
                            final String rawValue = ((XSSFCell) cell).getRawValue();
                            if (isPositiveNumber(rawValue)) {
                                setter.invoke(obj, rawValue);
                            } else {
                                throw new ServiceException(new ServiceMessage("rate.positive_number_required"));
                            }
                        }
                        case DATE -> setter.invoke(obj, cell.getDateCellValue().toInstant().toString());
                        default -> setter.invoke(obj, cell.getStringCellValue());
                    }
                }
            }
        }
        return obj;
    }

    private boolean isPositiveNumber(String rawValue) {
        String regexp = "^[1-9]+[0-9]*$";
        final Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(rawValue);
        return matcher.matches();
    }

    private LocalDate parseDateFromInstantString(String rawValue) {
        ZonedDateTime zonedDateTime = Instant.parse(rawValue).atZone(ZoneId.systemDefault());
        String formattedDate = zonedDateTime.toLocalDate().format(LOCAL_DATE_FORMATTER);
        return LocalDate.parse(formattedDate);
    }

    private Object findLinkRelation(String xlsxStringValue, XlsxHeader header) {
        return switch (header.getDictionaryLink()) {
            case CURRENCY -> currencyRepository.findFirstByName(xlsxStringValue).orElseThrow(() -> {
                throw new ServiceException(new ServiceMessage("dictionary_relation_not_found", CURRENCY.name(), xlsxStringValue));
            });
            case IMP -> impRepository.findFirstByCode(xlsxStringValue).orElseThrow(() -> {
                throw new ServiceException(new ServiceMessage("dictionary_relation_not_found", IMP.name(), xlsxStringValue));
            });
            case AIRPORT -> airportRepository.findFirstByCode(xlsxStringValue).orElseThrow(() -> {
                throw new ServiceException(new ServiceMessage("dictionary_relation_not_found", AIRPORT.name(), xlsxStringValue));
            });
            case AIRLINE -> airlineRepository.findFirstByAirlineCode(xlsxStringValue).orElseThrow(() -> {
                throw new ServiceException(new ServiceMessage("dictionary_relation_not_found", AIRLINE.name(), xlsxStringValue));
            });
            default -> null;
        };
    }
}