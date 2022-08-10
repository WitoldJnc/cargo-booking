package com.cargo.booking.calculate.dto;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class OfferRequestFullDto extends OfferRequestDto {
    private Set<CargoPartDto> cargoParts = new LinkedHashSet<>();
}
