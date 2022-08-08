package com.cargo.booking.auth.dto.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantUserDto {
    private Boolean administrator;
    private ParticipantDto participant;
    private List<WorkspaceRoleDto> workspaceRoles = new ArrayList<>();
}
