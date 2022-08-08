package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.model.ParticipantStatus;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import java.util.UUID;

@Data
public class ParticipantDto {
    private UUID id;
    private String name;
    private ParticipantType type;
    private ParticipantStatus status;
}
