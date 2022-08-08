package com.cargo.booking.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
    Optional<Currency> findFirstByName(String name);
}