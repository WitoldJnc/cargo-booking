package com.cargo.booking.auth.dto.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkspaceRole {
    private String workspaceName;
    private String roleName;
    private List<String> authorities = new ArrayList<>();
}
