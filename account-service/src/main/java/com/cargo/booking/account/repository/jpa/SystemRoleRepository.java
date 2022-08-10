package com.cargo.booking.account.repository.jpa;

import com.cargo.booking.account.model.SystemRole;
import com.cargo.booking.account.model.SystemRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface SystemRoleRepository extends JpaRepository<SystemRole, UUID> {
    SystemRole getByName(SystemRoleName name);
    Set<SystemRole> getByNameIn(Set<SystemRoleName> names);
}
