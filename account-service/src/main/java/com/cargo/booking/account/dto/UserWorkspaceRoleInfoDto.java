package com.cargo.booking.account.dto;

import com.cargo.booking.account.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWorkspaceRoleInfoDto {
    private UserInfoDto user;
    private UUID workspaceRoleId;
}
