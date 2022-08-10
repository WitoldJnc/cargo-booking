package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.company.ShortCompanyDto;
import com.cargo.booking.account.model.ParticipantStatus;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class DetailedParticipantDto {
    private UUID id;
    private ParticipantType type;
    private ParticipantStatus status;
    private ShortCompanyDto company;
    private Set<String> workspacesNames;
}
