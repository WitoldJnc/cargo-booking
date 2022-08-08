package com.cargo.booking.account.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserWorkspaceRoleDto {

    @NotNull(message = "{participant.empty_workspace_user}")
    private UUID userId;

    @NotNull(message = "{participant.empty_workspace}")
    private UUID workspaceRoleId;
}
