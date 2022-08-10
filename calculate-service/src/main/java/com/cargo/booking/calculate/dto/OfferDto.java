package com.cargo.booking.calculate.dto;

import com.cargo.booking.calculate.dto.participant.CompanyDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OfferDto {
    private UUID id;
    private CompanyDto company;
    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String status;
    private String shippingCost;
    private String currencySymbol;
    private TravelTime travelTime;
    private String shipmentName;
    private List<TransitStation> transitStation = new ArrayList<>();
    private LocalDateTime createDt;
    private LocalDateTime expirationDt;
}
