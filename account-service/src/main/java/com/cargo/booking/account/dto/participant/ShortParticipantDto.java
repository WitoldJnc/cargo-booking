package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.company.ShortCompanyDto;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ShortParticipantDto {
    private UUID id;
    private ParticipantType type;
    private ShortCompanyDto company;
    private Set<UUID> workspaceRolesIds = new HashSet<>();
}
