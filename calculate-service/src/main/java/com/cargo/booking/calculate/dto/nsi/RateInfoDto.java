package com.cargo.booking.calculate.dto.nsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RateInfoDto {
    private UUID id;
    private UUID participantId;
    private UUID airline;
    private UUID departure;
    private UUID arrival;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String impCode;
    private Integer minWeight;
    private Integer lessThan45Kg;
    private Integer from46To100Kg;
    private Integer from101To300Kg;
    private Integer from301To1000Kg;
    private Integer over1001Kg;
    private Integer airbillCost;
    private String currencySymbol;
}
