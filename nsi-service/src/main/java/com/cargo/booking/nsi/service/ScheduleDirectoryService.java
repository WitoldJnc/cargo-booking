package com.cargo.booking.nsi.service;

import com.cargo.booking.nsi.repository.ScheduleRepository;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import com.cargo.booking.nsi.dto.ScheduleDto;
import com.cargo.booking.nsi.dto.ScheduleRequestDto;
import com.cargo.booking.nsi.mapper.ScheduleMapper;
import com.cargo.booking.nsi.model.Aircraft;
import com.cargo.booking.nsi.model.Airline;
import com.cargo.booking.nsi.model.Airport;
import com.cargo.booking.nsi.model.Schedule;
import com.cargo.booking.nsi.parser.ElementSSIM;
import com.cargo.booking.nsi.parser.ParserIataSsim;
import com.cargo.booking.nsi.parser.RecordTypeSSIM;
import com.cargo.booking.nsi.repository.AircraftRepository;
import com.cargo.booking.nsi.repository.AirlineRepository;
import com.cargo.booking.nsi.repository.AirportRepository;
import com.cargo.booking.nsi.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;


@Slf4j
@Service
@AllArgsConstructor
public class ScheduleDirectoryService {
    private final ScheduleRepository scheduleRepository;
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final ScheduleMapper scheduleMapper;

    public ServiceMessage scheduleUpload(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        String file;
        try {
            file = new String(multipartFile.getInputStream().readAllBytes());
        } catch (IOException ex) {
            throw new ServiceException(new ServiceMessage("schedule.upload_file_error"));
        }
        String[] lines = splitFileToRows(file);
        Airline airline = getAirlineSchedule(lines, fileName);
        Set<Schedule> schedules = getSchedules(lines, fileName);

        saveSchedules(schedules, airline, fileName);
        return new ServiceMessage("schedule.success_upload_file", schedules.size(), fileName);
    }

    public String[] splitFileToRows(String file) {
        return file.split("\r\n");
    }

    public Airline getAirlineSchedule(String[] lines, String fileName) {
        for (String line : lines) {
            String recordTypeNumber = ParserIataSsim.parse(line, ElementSSIM.RECORD_TYPE_NUMBER).getRawValue();
            if (RecordTypeSSIM.CARRIER.getRecordTypeNumber().equals(recordTypeNumber)) {
                return getAirline(line, fileName);
            }
        }
        return null;
    }

    public Set<Schedule> getSchedules(String[] lines, String fileName) {
        Set<Schedule> schedules = new LinkedHashSet<>();
        for (String line : lines) {
            String recordTypeNumber = ParserIataSsim.parse(line, ElementSSIM.RECORD_TYPE_NUMBER).getRawValue();
            if (RecordTypeSSIM.FLIGHT_LEG.getRecordTypeNumber().equals(recordTypeNumber)) {
                try {
                    schedules.add(getSchedule(line, fileName));
                } catch (ParseException | ValidationException e) {
                    String lineNumber = ParserIataSsim.parse(line, ElementSSIM.LINE_NUMBER).getRawValue();
                    throw new ServiceException(new ServiceMessage("schedule.upload_file_line_error",
                            lineNumber,
                            fileName));
                }
            } else if (RecordTypeSSIM.SEGMENT_DATA.getRecordTypeNumber().equals(recordTypeNumber)) {
                break;
            }
        }
        return schedules;
    }

    void clearScheduleByAirlineId(UUID airlineId) {
        scheduleRepository.deleteAllByAirlineId(airlineId);
    }

    private Schedule getSchedule(String line, String fileName) throws ParseException {
        Schedule schedule = new Schedule();
        schedule.setOperationalSuffix(ParserIataSsim.parse(line, ElementSSIM.OPERATIONAL_SUFFIX).getRawValue().isEmpty() ? null : ParserIataSsim.parse(line, ElementSSIM.OPERATIONAL_SUFFIX).getRawValue().charAt(0));
        schedule.setAirlineId(getAirline(line, fileName));
        schedule.setFlightNumber(ParserIataSsim.parse(line, ElementSSIM.FLIGHT_NUMBER).getRawValue());
        schedule.setIvi(ParserIataSsim.parse(line, ElementSSIM.IVI).getRawValue());
        schedule.setLegSequenceNumber(ParserIataSsim.parse(line, ElementSSIM.LEG_SEQ_NUMBER).getRawValue());
        schedule.setServiceType(ParserIataSsim.parse(line, ElementSSIM.SERVICE_TYPE).getRawValue().charAt(0));
        schedule.setAircraftId(getAircraft(line, fileName));
        schedule.setPeriodOfOperationFrom(getDate(line, ElementSSIM.PERIOD_FROM));
        schedule.setPeriodOfOperationTo(getDate(line, ElementSSIM.PERIOD_TO));
        schedule.setDaysOfOperation(ParserIataSsim.parse(line, ElementSSIM.DAYS_OF_OPERATION).getFormatValue());
        schedule.setDepartureStation(getAirport(line, ElementSSIM.DEPARTURE_STATION, fileName));
        schedule.setDepartureTimeAircraftTz(getZonedTime(line, ElementSSIM.AIRCRAFT_STD));
        schedule.setArrivalStation(getAirport(line, ElementSSIM.ARRIVAL_STATION, fileName));
        schedule.setArrivalTimeAircraft(getZonedTime(line, ElementSSIM.AIRCRAFT_STA));
        return schedule;
    }

    @Transactional
    public void saveSchedules(Set<Schedule> schedules, Airline airline, String fileName) {
        if (schedules == null || airline == null) {
            throw new ServiceException(new ServiceMessage("schedule.error_empty_file"));
        }
        clearScheduleByAirlineId(airline.getId());
        saveSchedule(schedules);
    }

    public ScheduleDto getScheduleById(UUID scheduleId){
       return scheduleMapper.scheduleToScheduleDto(scheduleRepository.findById(scheduleId).orElseThrow(() ->{
           throw new ServiceException(new ServiceMessage("schedule.not_found", scheduleId));
       }));
    }

    public List<ScheduleDto> findSchedulesByCities(ScheduleRequestDto requestDto) {
        long dateFrom = requestDto.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        List<Schedule> schedules = scheduleRepository.findAllByDepartureArriveCities(requestDto.getDepartureId(), requestDto.getArrivalId()).stream()
                .filter(schedule ->
                        (dateFrom > schedule.getPeriodOfOperationFrom().toEpochMilli() && dateFrom < schedule.getPeriodOfOperationTo().toEpochMilli()) &&
                        (schedule.getDaysOfOperation().contains(requestDto.getDayOfWeek())) &&
                        (requestDto.getAllowedAirlines().contains(schedule.getAirlineId().getId()))
                )
                .toList();
        return scheduleMapper.schedulesToScheduleDtos(schedules);
    }

    private void saveSchedule(Set<Schedule> schedules) {
        scheduleRepository.saveAll(schedules);
    }

    private Instant getDate(String line, ElementSSIM element) throws ParseException {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMMyy", Locale.US);
        return sdfDate.parse(ParserIataSsim.parse(line, element).getRawValue()).toInstant();
    }

    private Instant getZonedTime(String line, ElementSSIM element) throws ParseException {
        final SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmZ", Locale.US);
        return sdfTime.parse(ParserIataSsim.parse(line, element).getRawValue()).toInstant();
    }

    private Airline getAirline(String line, String fileName) {
        String airlineCode = ParserIataSsim.parse(line, ElementSSIM.AIRLINE_CODE).getRawValue();
        return airlineRepository.findFirstByAirlineCode(airlineCode)
                .orElseThrow(() -> {
                    String lineNumber = ParserIataSsim.parse(line, ElementSSIM.LINE_NUMBER).getRawValue();
                    return new ServiceException(new ServiceMessage(
                            "schedule.error_airline_not_found", airlineCode, lineNumber, fileName));
                });
    }

    private Aircraft getAircraft(String line, String fileName) {
        String aircraftTypeIata = ParserIataSsim.parse(line, ElementSSIM.AIRCRAFT_IATA_TYPE).getRawValue();
        return aircraftRepository.findFirstByTypeIata(aircraftTypeIata)
                .orElseThrow(() -> {
                    String lineNumber = ParserIataSsim.parse(line, ElementSSIM.LINE_NUMBER).getRawValue();
                    return new ServiceException(new ServiceMessage(
                            "schedule.error_aircraft_not_found", aircraftTypeIata, lineNumber, fileName));
                });
    }

    private Airport getAirport(String line, ElementSSIM element, String fileName) {
        String airportCode = ParserIataSsim.parse(line, element).getRawValue();
        return airportRepository.findFirstByCode(airportCode)
                .orElseThrow(() -> {
                    String lineNumber = ParserIataSsim.parse(line, ElementSSIM.LINE_NUMBER).getRawValue();
                    return new ServiceException(new ServiceMessage(
                            "schedule.error_airport_not_found", airportCode, lineNumber, fileName));
                });
    }
}
