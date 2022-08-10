package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.SettingsDto;
import com.cargo.booking.account.dto.UserWorkspaceRoleInfoDto;
import com.cargo.booking.account.dto.company.CompanyDto;
import com.cargo.booking.account.model.ParticipantStatus;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantInfoDto {
    private ParticipantType type;
    private ParticipantStatus status;
    private CompanyDto company;
    private List<UserWorkspaceRoleInfoDto> workspaces = new ArrayList<>();
    private SettingsDto settings;
}
