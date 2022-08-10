package com.cargo.booking.account.repository.jpa;

import com.cargo.booking.account.model.ParticipantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface ParticipantUserRepository extends JpaRepository<ParticipantUser, UUID> {

    @Query(value = """
            select u.email from participant_user pu
            join participant_user_workspace_role puwr on pu.id = puwr.participant_user_id
            join users u on pu.user_id = u.id
            where pu.participant_id = :participantId
            and puwr.workspace_role_id = :workspaceRoleId
            and u.blocked is false
                        """, nativeQuery = true)
    Set<String> getEmailsByParticipantIdAndWorkspaceRoleId(UUID participantId, UUID workspaceRoleId);
}
