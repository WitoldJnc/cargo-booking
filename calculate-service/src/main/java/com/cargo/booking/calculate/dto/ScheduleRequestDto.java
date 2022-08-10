package com.cargo.booking.calculate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduleRequestDto {
    private UUID departureId;
    private UUID arrivalId;
    private List<UUID> allowedAirlines;
    private String dayOfWeek;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
}
