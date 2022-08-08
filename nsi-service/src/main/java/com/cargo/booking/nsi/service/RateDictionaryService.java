package com.cargo.booking.nsi.service;

import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import com.cargo.booking.nsi.dto.RateInfoDto;
import com.cargo.booking.nsi.dto.RatePreviewDto;
import com.cargo.booking.nsi.dto.XlsxRateDictionaryDto;
import com.cargo.booking.nsi.exception.HandleXlsxInputStreamException;
import com.cargo.booking.nsi.exception.MultipartFileIsEmptyException;
import com.cargo.booking.nsi.exception.RequiredDictionaryFieldNotPresentException;
import com.cargo.booking.nsi.exception.XlsxToObjProcessException;
import com.cargo.booking.nsi.mapper.RateMapper;
import com.cargo.booking.nsi.model.Rate;
import com.cargo.booking.nsi.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateDictionaryService {
    private final RateRepository rateRepository;
    private final XlsxParser<XlsxRateDictionaryDto, Rate> xlsxParser;
    private final RateMapper rateMapper;

    @Transactional
    public List<Rate> saveNewDictionaryRecordsByParticipant(MultipartFile multipartFile, Optional<UUID> maybeParticipantId) {
        if (maybeParticipantId.isEmpty()) {
            throw new ServiceException(new ServiceMessage("rate.participant_id_not_present"));
        }
        UUID participantId = maybeParticipantId.get();
        LocalDateTime dtUpload = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Rate> rates = parseDictionaryXlsx(multipartFile);

        rateRepository.markOldRecordsAsInactive(participantId);

        return rates.stream()
                .peek(entity -> {
                    entity.setDtUpload(dtUpload);
                    entity.setParticipantId(participantId);
                    entity.setFileName(multipartFile.getOriginalFilename());
                    entity.setIsActive(true);
                })
                .map(rateRepository::save)
                .toList();
    }

    public List<RatePreviewDto> findRateRecordsByParticipant(Optional<UUID> participantId) {
        if (participantId.isEmpty()) {
            throw new ServiceException(new ServiceMessage("rate.paticipant_id_not_present"));
        }
        return rateRepository.findAllByParticipantIdOrderByDtUpload(participantId.get()).stream()
                .map(rateMapper::rateToRatePreviewDto)
                .distinct()
                .sorted(Comparator.comparing(RatePreviewDto::getDtUpload)
                        .reversed())
                .toList();
    }

    private List<Rate> parseDictionaryXlsx(MultipartFile multipartFile) {
        List<XlsxRateDictionaryDto> rateDirectoryDtos;
        try {
            rateDirectoryDtos = xlsxParser.parse(multipartFile, XlsxRateDictionaryDto.class);
        } catch (RequiredDictionaryFieldNotPresentException e) {
            throw new ServiceException(new ServiceMessage("rate.required_field_not_present_" + e.getRequiredField(),
                    e.getRowNum(), multipartFile.getOriginalFilename()));
        } catch (HandleXlsxInputStreamException e) {
            throw new ServiceException(new ServiceMessage("rate.cant_handle_input_stream", e));
        } catch (XlsxToObjProcessException e) {
            throw new ServiceException(new ServiceMessage("rate.cant_xlsx_to_obj_process", e));
        } catch (MultipartFileIsEmptyException e) {
            throw new ServiceException(new ServiceMessage("nsi.file_is_empty", multipartFile.getOriginalFilename()));
        }

        return rateDirectoryDtos.stream()
                .map(dto -> {
                    try {
                        return xlsxParser.convertXlsxDtoToEntity(dto, new Rate());
                    } catch (XlsxToObjProcessException e) {
                        log.error(e.getMessage(), e);
                        throw new ServiceException(new ServiceMessage("rate.cant_xlsx_to_obj_process", e));
                    }
                }).toList();
    }

    public List<RateInfoDto> findRatesByCities(UUID departureId, UUID arriveId) {
        return rateMapper.ratesToRateInfoDtos(rateRepository.findAllActiveByArriveAndDepartureCities(departureId, arriveId));
    }
}