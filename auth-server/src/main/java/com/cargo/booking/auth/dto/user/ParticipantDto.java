package com.cargo.booking.auth.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class ParticipantDto {
    private UUID id;
    private String name;
    private ParticipantType type;
}
