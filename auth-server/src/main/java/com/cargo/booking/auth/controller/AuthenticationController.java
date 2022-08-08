package com.cargo.booking.auth.controller;

import com.cargo.booking.auth.dto.*;
import com.cargo.booking.auth.dto.password.ChangePasswordRequestDto;
import com.cargo.booking.auth.dto.password.RecoveryPasswordDto;
import com.cargo.booking.auth.dto.password.RecoveryPasswordRequestDto;
import com.cargo.booking.auth.dto.registration.UserRegistrationDto;
import com.cargo.booking.auth.dto.registration.VerificationCodeRequestDto;
import com.cargo.booking.auth.service.AuthenticationService;
import com.cargo.booking.auth.service.ChangePasswordService;
import com.cargo.booking.auth.service.RegistrationService;
import com.cargo.booking.messages.rest.RestMessage;
import com.cargo.booking.messages.rest.RestMessageFormatter;
import com.cargo.booking.starter.auth.ParticipantAuthenticationToken;
import com.cargo.booking.starter.jwt.JwtClaims;
import com.cargo.booking.starter.jwt.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cargo.booking.starter.jwt.JwtRequestFilter.BEARER_STRING_LENGTH;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@ComponentScan("com.cargo.booking.messages")
public class AuthenticationController {

    private final ChangePasswordService changePasswordService;
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final RestMessageFormatter messageFormatter;
    private final JwtUtils jwtUtils;

    @ApiOperation(value = "Authenticate in platform")
    @PostMapping("/login")
    public Token authenticate(@RequestBody LoginRequest request) {
        return authenticationService.authenticate(request.getUsername(), request.getPassword());
    }

    @ApiOperation(value = "Authorize in participant workspace", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping("/login/workspace")
    public ParticipantWorkspaceToken authorizeInParticipantWorkspace(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                     @RequestBody ParticipantWorkspaceRequest request) {
        JwtClaims jwtClaims = jwtUtils.getJwtTokenClaims(authorizationHeader.substring(BEARER_STRING_LENGTH));
        ParticipantAuthenticationToken authentication = (ParticipantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authenticationService.authorizeInParticipantWorkspace(String.valueOf(authentication.getPrincipal()),
                request.getParticipantId(), request.getWorkspaceRoleId(), jwtClaims.expiration());
    }

    @ApiOperation(value = "Refresh access token")
    @PostMapping("/token/refresh")
    public Token renewAuthentication(@RequestBody RefreshRequest request) {
        return authenticationService.renewAuthentication(request.getUsername(), request.getRefreshToken());
    }

    @ApiOperation(value = "Check token", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping("/token/check")
    public RestMessage validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String requestTokenHeader) {
        return messageFormatter.restMessage(authenticationService.validateToken(requestTokenHeader));
    }

    @ApiOperation(value = "Send registration verification code to email")
    @PostMapping("/register/verification-code")
    public RestMessage sendVerificationCode(@Valid @RequestBody VerificationCodeRequestDto request) {
        return messageFormatter.restMessage(registrationService.sendVerificationCodeTo(request.getEmail()));
    }

    @ApiOperation(value = "Register new user")
    @PostMapping("/register")
    public RestMessage register(@Valid @RequestBody UserRegistrationDto request) {
        return messageFormatter.restMessage(registrationService.registerNewUser(request));
    }

    @ApiOperation(value = "Send password recovery code to email")
    @PostMapping("/password/recovery/code")
    public RestMessage sendPasswordRecoveryCode(@Valid @RequestBody RecoveryPasswordRequestDto requestDto) {
        return messageFormatter.restMessage(changePasswordService.sendRecoveryCodeTo(requestDto.getEmail()));
    }

    @ApiOperation(value = "Recovery password by code")
    @PostMapping("/password/recovery")
    RestMessage recoveryPasswordByCode(@Valid @RequestBody RecoveryPasswordDto requestDto) {
        return messageFormatter.restMessage(changePasswordService.recoverPassword(requestDto));
    }

    @ApiOperation(value = "Change password", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping("/password/change")
    RestMessage changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        ParticipantAuthenticationToken authentication = (ParticipantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return messageFormatter.restMessage(changePasswordService.changePassword(requestDto, authentication.getPrincipal().toString()));
    }

}
