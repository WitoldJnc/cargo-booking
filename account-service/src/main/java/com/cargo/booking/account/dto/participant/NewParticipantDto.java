package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.SettingsDto;
import com.cargo.booking.account.dto.UserWorkspaceRoleDto;
import com.cargo.booking.account.dto.company.NewCompanyDto;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewParticipantDto {

    @NotNull(message = "{participant.empty_type}")
    private ParticipantType participantType;

    @Valid
    private NewCompanyDto company;

    @Valid
    @Size(min = 1, message = "{participant.no_workspaces}")
    private List<UserWorkspaceRoleDto> workspaces;

    private SettingsDto settings;
}
