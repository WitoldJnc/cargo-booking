package com.cargo.booking.claim.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class ClaimDto {
    private UUID id;
    private String name;
}
