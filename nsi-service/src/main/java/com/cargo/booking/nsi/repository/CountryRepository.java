package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
}
