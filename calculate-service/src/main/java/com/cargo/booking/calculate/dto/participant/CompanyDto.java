package com.cargo.booking.calculate.dto.participant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CompanyDto {
    private UUID id;
    private String shortName;
    private String logo;
}
