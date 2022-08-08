package com.cargo.booking.auth.service;

import com.cargo.booking.auth.client.AccountServiceClient;
import com.cargo.booking.auth.dto.ParticipantWorkspaceToken;
import com.cargo.booking.auth.dto.Token;
import com.cargo.booking.auth.dto.user.ParticipantDto;
import com.cargo.booking.auth.dto.user.ParticipantUserDto;
import com.cargo.booking.auth.dto.user.UserDto;
import com.cargo.booking.auth.dto.user.WorkspaceRoleDto;
import com.cargo.booking.auth.exception.InvalidCredentialsException;
import com.cargo.booking.auth.external.dto.AuthToken;
import com.cargo.booking.auth.jwt.JwtAuthUtils;
import com.cargo.booking.messages.MessageCode;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cargo.booking.starter.jwt.JwtRequestFilter.BEARER_STRING_LENGTH;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private static final String LOG_TEMPLATE = "[{}] {}";
    private final JwtAuthUtils jwtAuthUtils;
    private final com.cargo.booking.auth.external.AuthenticationService dmeAuthenticationService;
    private final AccountServiceClient accountServiceClient;

    public Token authenticate(String username, String password) {
        try {
            AuthToken dmeToken = dmeAuthenticationService.login(username, password);
            Token token = authenticateInternal(username, dmeToken);
            return token;
        } catch (InvalidCredentialsException ex) {
            throw new ServiceException(new ServiceMessage("auth.invalid_credentials", username));
        }
    }

    public ParticipantWorkspaceToken authorizeInParticipantWorkspace(String username, UUID participantId,
                                                                     UUID workspaceRoleId, Date expiration) {
        UserDto user = getUser(username);
        Optional<ParticipantUserDto> maybePU = user.getParticipantUsers().stream()
                .filter(pu -> pu.getParticipant().getId().equals(participantId))
                .findFirst();
        Optional<WorkspaceRoleDto> maybeWR = maybePU.flatMap(pu -> pu.getWorkspaceRoles().stream()
                .filter(wr -> wr.getId().equals(workspaceRoleId))
                .findFirst());
        Set<String> authorities = maybeWR.map(WorkspaceRoleDto::getAuthorities).orElse(Collections.emptySet());
        Set<String> allAuthorities = new HashSet<>();
        allAuthorities.addAll(authorities);
        allAuthorities.addAll(user.getSystemAuthorities());
        String accessToken = jwtAuthUtils.generateAccessToken(username,
                maybePU.map(pu -> participantId),
                maybeWR.map(wr -> workspaceRoleId),
                authorities, expiration);
        return new ParticipantWorkspaceToken(participantId, workspaceRoleId, accessToken, allAuthorities);
    }

    public Token renewAuthentication(String username, String refreshToken) {
        try {
            AuthToken dmeToken = dmeAuthenticationService.refresh(username, refreshToken);
            return authenticateInternal(username, dmeToken);
        } catch (InvalidCredentialsException ex) {
            throw new ServiceException(new ServiceMessage("auth.invalid_credentials", username));
        }
    }

    public Token authenticateInternal(String username, AuthToken dmeToken) {
        UserDto user = getUser(username);
        String accessToken = jwtAuthUtils.generateAccessToken(username, user.getSystemAuthorities(), dmeToken.getTokenLifeTime());
        return new Token(username, accessToken, dmeToken.getRefreshToken(), user);
    }

    public ServiceMessage validateToken(String requestTokenHeader) {
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(BEARER_STRING_LENGTH);
            if (jwtAuthUtils.validateToken(jwtToken)) {
                return new ServiceMessage(MessageCode.TOKEN_VALID);
            }
        }
        return new ServiceMessage(MessageCode.TOKEN_INVALID);
    }

    private UserDto getUser(String username) {
        UserDto user = accountServiceClient.findUserByEmail(username, LocaleContextHolder.getLocale().getLanguage())
                .orElseThrow(() -> {
                    return new ServiceException(new ServiceMessage("auth.invalid_credentials", username));
                });
        if (Boolean.TRUE.equals(user.getBlocked())) {
            throw new ServiceException(new ServiceMessage("auth.user_blocked"));
        }
        return user;
    }
}
