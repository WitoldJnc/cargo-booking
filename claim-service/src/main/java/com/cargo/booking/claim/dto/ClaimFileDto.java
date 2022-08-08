package com.cargo.booking.claim.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ClaimFileDto {
    private UUID id;
    private String name;
}
