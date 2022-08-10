package com.cargo.booking.account.repository;

import com.cargo.booking.account.model.Participant;
import com.cargo.booking.account.model.ParticipantStatus;
import com.cargo.booking.account.model.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParticipantDao {
    Page<Participant> search(String query, ParticipantStatus participantStatus,
                             ParticipantType participantType, Pageable pageable);
}
