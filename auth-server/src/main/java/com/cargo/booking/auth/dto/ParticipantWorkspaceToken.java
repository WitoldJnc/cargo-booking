package com.cargo.booking.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ParticipantWorkspaceToken {
    private UUID participantId;
    private UUID workspaceRoleId;
    private String accessToken;
    private Set<String> authorities;
}
