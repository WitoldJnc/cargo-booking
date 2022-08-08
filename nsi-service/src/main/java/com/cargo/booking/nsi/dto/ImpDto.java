package com.cargo.booking.nsi.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ImpDto {
    private UUID id;
    private String code;
    private String name;
}
