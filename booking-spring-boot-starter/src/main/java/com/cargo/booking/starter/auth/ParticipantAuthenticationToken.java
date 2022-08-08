package com.cargo.booking.starter.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ParticipantAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * Идентификатор участника
     */
    private Optional<UUID> participant;

    /**
     * Идентификатор ЛК и роли в нём
     */
    private Optional<UUID> workspaceRole;

    private String authorizationToken;

    public ParticipantAuthenticationToken(Object principal, Object credentials,
                                          Collection<? extends GrantedAuthority> authorities,
                                          String authorizationToken) {
        super(principal, credentials, authorities);
        this.authorizationToken = authorizationToken;
    }

    public ParticipantAuthenticationToken(Object principal, Object credentials,
                                          Collection<? extends GrantedAuthority> authorities,
                                          Optional<UUID> participant, Optional<UUID> workspaceRole,
                                          String authorizationToken) {
        super(principal, credentials, authorities);
        this.participant = participant;
        this.workspaceRole = workspaceRole;
        this.authorizationToken = authorizationToken;
    }

    public Optional<UUID> getParticipant() {
        return participant;
    }

    public Optional<UUID> getWorkspaceRole() {
        return workspaceRole;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }
}
