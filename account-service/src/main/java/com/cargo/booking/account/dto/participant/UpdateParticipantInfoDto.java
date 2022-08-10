package com.cargo.booking.account.dto.participant;

import com.cargo.booking.account.dto.company.NewCompanyDto;
import com.cargo.booking.account.model.ParticipantType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UpdateParticipantInfoDto {

    @NotNull(message = "{participant.empty_type}")
    private ParticipantType participantType;

    @Valid
    private NewCompanyDto company;
}
