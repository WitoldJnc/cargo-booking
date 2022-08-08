package com.cargo.booking.nsi.service;

import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.nsi.model.*;
import com.cargo.booking.nsi.repository.AircraftRepository;
import com.cargo.booking.nsi.repository.AirlineRepository;
import com.cargo.booking.nsi.repository.AirportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleDirectoryServiceTest {
    @InjectMocks
    private ScheduleDirectoryService scheduleDirectoryService;

    @Mock
    private AircraftRepository aircraftRepository;
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private AirportRepository airportRepository;

    @Test
    void scheduleUploadTest() {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(UUID.randomUUID());
        aircraft.setType(new JsonLocale("TU-134", "ТУ-134"));
        aircraft.setTypeIata("32A");
        when(aircraftRepository.findFirstByTypeIata(any(String.class))).thenReturn(java.util.Optional.of(aircraft));

        Airport airport = new Airport();
        airport.setId(UUID.randomUUID());
        airport.setCityId(new City());
        airport.setCode("DME");
        airport.setCountryId(new Country());
        airport.setName(new JsonLocale("Domodedovo", "Домодедово"));
        when(airportRepository.findFirstByCode(any(String.class))).thenReturn(java.util.Optional.of(airport));

        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setAirlineCode("S7");
        airline.setName(new JsonLocale("S7 Airlines", "С7 Сибирь"));
        when(airlineRepository.findFirstByAirlineCode(any(String.class))).thenReturn(java.util.Optional.of(airline));

        String file = null;
        try {
            file = Files.readString(Path.of("src/test/resources/SSIM_short.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(file);

        String[] lines = scheduleDirectoryService.splitFileToRows(file);
        assertNotNull(lines);
        assertEquals(40, lines.length);

        try {
            Set<Schedule> schedules = scheduleDirectoryService.getSchedules(lines, "SSIM_short.txt");
            assertEquals(15, schedules.size());
            assertEquals(3, schedules.stream().filter(s -> "1006".equals(s.getFlightNumber())).count());
            assertEquals(1, schedules.stream().filter(s -> "1006".equals(s.getFlightNumber()) && "03".equals(s.getIvi())).count());
        } catch (ServiceException e) {
            assertNull(e, "invalid line: " + e.getServiceMessage().getArgs()[0]);
        }
    }
}