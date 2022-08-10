package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.UserWorkspaceRoleDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateParticipantWorkspacesDto {

    @Valid
    @Size(min = 1, message = "{participant.no_workspaces}")
    private Set<UserWorkspaceRoleDto> workspaces;
}
