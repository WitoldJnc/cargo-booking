package com.cargo.booking.auth.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ParticipantWorkspaceRequest {
    private UUID participantId;
    private UUID workspaceRoleId;
}
