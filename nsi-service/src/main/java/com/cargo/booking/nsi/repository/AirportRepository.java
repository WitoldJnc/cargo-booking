package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AirportRepository extends JpaRepository<Airport, UUID> {
    Optional<Airport> findFirstByCode(String code);
}
