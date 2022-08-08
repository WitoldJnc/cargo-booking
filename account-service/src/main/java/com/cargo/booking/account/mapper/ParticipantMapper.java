package com.cargo.booking.account.mapper;

import com.cargo.booking.account.dto.participant.ParticipantDto;
import com.cargo.booking.account.dto.participant.ShortParticipantDto;
import com.cargo.booking.account.model.Participant;
import com.cargo.booking.account.model.ParticipantType;
import org.mapstruct.*;

@Mapper(uses = CompanyMapper.class)
public abstract class ParticipantMapper {

    public abstract ParticipantDto participantToParticipantDto(Participant participant);


    @AfterMapping
    protected void afterParticipantToParticipantDto(@MappingTarget ParticipantDto dto, Participant participant) {
        if (participant.getType().equals(ParticipantType.COMPANY) && participant.getCompany() != null) {
            dto.setName(participant.getCompany().getShortName());
        }
    }

    public abstract ShortParticipantDto participantToShortParticipantDto(Participant participant);
}
