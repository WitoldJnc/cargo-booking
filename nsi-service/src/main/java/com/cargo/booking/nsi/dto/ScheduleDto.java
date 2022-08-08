package com.cargo.booking.nsi.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ScheduleDto {
    private UUID id;
    private Character operationalSuffix;
    private String airlineDesignator;
    private String flightNumber;
    private String itineraryVariationIdentifier;
    private String legSequenceNumber;
    private char serviceType;
    private Instant periodOfOperationFrom;
    private Instant periodOfOperationTo;
    private String daysOfOperation;
    private String departureStation;
    private Instant aircraftSTDtz;
    private String arrivalStation;
    private Instant aircraftSTAtz;
    private LocalTime arrivalTimeAircraft;
    private LocalTime departureTimeAircraftTz;
}
