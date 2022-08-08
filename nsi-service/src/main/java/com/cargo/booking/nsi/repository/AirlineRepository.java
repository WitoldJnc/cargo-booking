package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AirlineRepository extends JpaRepository<Airline, UUID> {
    Optional<Airline> findFirstByAirlineCode(String airlineCode);
}
