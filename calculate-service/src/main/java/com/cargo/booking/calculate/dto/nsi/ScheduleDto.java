package com.cargo.booking.calculate.dto.nsi;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class ScheduleDto {
    private UUID id;
    private String departureStation;
    private String arrivalStation;
    private LocalTime arrivalTimeAircraft;
    private LocalTime departureTimeAircraftTz;
}
