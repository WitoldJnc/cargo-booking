package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.WorkspaceRoleDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantUserDto {
    private Boolean administrator;
    private ParticipantDto participant;
    private List<WorkspaceRoleDto> workspaceRoles = new ArrayList<>();
}
