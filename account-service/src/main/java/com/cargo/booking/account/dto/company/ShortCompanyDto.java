package com.cargo.booking.account.dto.company;

import lombok.Data;

import java.util.UUID;

@Data
public class ShortCompanyDto {
    private UUID id;
    private String inn;
    private String shortName;
}
