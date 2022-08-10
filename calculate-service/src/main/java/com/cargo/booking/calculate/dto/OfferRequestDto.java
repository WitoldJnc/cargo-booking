package com.cargo.booking.calculate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OfferRequestDto {
    /**
     * Arrival city id
     */
    @NotNull(message = "{request.empty_arrival_id}")
    private UUID arrivalId;

    /**
     * Departure city id
     */
    @NotNull(message = "{request.empty_departure_id}")
    private UUID departureId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    private String imp;
    private String shipmentName;
}
