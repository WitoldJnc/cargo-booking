package com.cargo.booking.starter.jwt;

import com.cargo.booking.starter.auth.ParticipantAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public final class JwtRequestFilter extends OncePerRequestFilter {

    public static final int BEARER_STRING_LENGTH = 7;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        JwtClaims claims = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(BEARER_STRING_LENGTH);
            try {
                claims = jwtUtils.getJwtTokenClaims(jwtToken);
            } catch (IllegalArgumentException e) {
                log.debug("Не удалось разобрать JWT Token");
            } catch (ExpiredJwtException e) {
                log.debug("JWT Token истёк");
            }
        } else {
            log.debug("Не указан JWT Token");
        }

        if (claims != null && claims.subject() != null && !claims.subject().isEmpty()) {
            List<SimpleGrantedAuthority> grantedAuthorities = claims.authorities().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            SecurityContextHolder.getContext().setAuthentication(
                    new ParticipantAuthenticationToken(claims.subject(), null, grantedAuthorities,
                            claims.participant(), claims.workspaceRole(), requestTokenHeader.startsWith("Bearer ") ? requestTokenHeader : ""));
        }

        filterChain.doFilter(request, response);
    }
}
