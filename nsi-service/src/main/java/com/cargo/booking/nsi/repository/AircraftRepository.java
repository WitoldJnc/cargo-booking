package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {
    Optional<Aircraft> findFirstByTypeIata(String typeIta);
}
