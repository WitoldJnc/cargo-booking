package com.cargo.booking.account.repository.jpa;

import com.cargo.booking.account.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
