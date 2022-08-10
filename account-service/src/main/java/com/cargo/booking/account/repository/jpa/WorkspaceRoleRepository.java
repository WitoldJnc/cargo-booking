package com.cargo.booking.account.repository.jpa;

import com.cargo.booking.account.model.WorkspaceRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, UUID> {
}
