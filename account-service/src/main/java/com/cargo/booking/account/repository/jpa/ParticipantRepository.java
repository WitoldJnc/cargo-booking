package com.cargo.booking.account.repository.jpa;

import com.cargo.booking.account.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
}
