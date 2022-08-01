package com.cargo.booking.starter.jwt;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record JwtClaims(String subject, Optional<UUID> participant, Optional<UUID> workspaceRole,
                        List<String> authorities, Date expiration) {
}
