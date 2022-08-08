package com.cargo.booking.claim.repository;

import com.cargo.booking.claim.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {
}
