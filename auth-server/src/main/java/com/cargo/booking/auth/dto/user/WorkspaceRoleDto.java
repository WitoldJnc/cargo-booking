package com.cargo.booking.auth.dto.user;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class WorkspaceRoleDto {
    private UUID id;
    private String workspaceName;
    private String roleName;
    private Set<String> authorities = new HashSet<>();
}
