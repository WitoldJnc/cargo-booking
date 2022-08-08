package com.cargo.booking.account.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class WorkspaceRoleDto {
    private UUID id;
    private UUID workspaceId;
    private String workspaceName;
    private UUID roleId;
    private String roleName;
    private List<String> authorities = new ArrayList<>();
}
