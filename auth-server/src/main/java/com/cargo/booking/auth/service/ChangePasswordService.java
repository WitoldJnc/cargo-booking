package com.cargo.booking.auth.service;

import com.cargo.booking.auth.client.AccountServiceClient;
import com.cargo.booking.auth.dto.password.ChangePasswordRequestDto;
import com.cargo.booking.auth.dto.password.RecoveryPasswordDto;
import com.cargo.booking.auth.dto.user.UserDto;
import com.cargo.booking.auth.external.AuthenticationService;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChangePasswordService {

    private final AccountServiceClient accountServiceClient;

    private final AuthenticationService dmeAuthenticationService;

    public ServiceMessage sendRecoveryCodeTo(String email) {
        Optional<UserDto> userByEmail = accountServiceClient.findUserByEmail(email, LocaleContextHolder.getLocale().getLanguage());
        if (userByEmail.isEmpty()) {
            throw new ServiceException(new ServiceMessage("pass_change.user_not_found"));
        } else if (userByEmail.get().getBlocked()) {
            throw new ServiceException(new ServiceMessage("pass_change.user_is_blocked"));
        }
        dmeAuthenticationService.sendRecoveryCode(email);
        return new ServiceMessage("verification_code_sent");
    }

    public ServiceMessage recoverPassword(RecoveryPasswordDto requestDto) {
        dmeAuthenticationService.changePasswordByCode(requestDto.getEmail(), requestDto.getNewPassword(), requestDto.getEmail());
        return new ServiceMessage("pass_change.password_successfully_changed");
    }

    public ServiceMessage changePassword(ChangePasswordRequestDto requestDto, String email) {
        if (requestDto.getOldPassword().equals(requestDto.getNewPassword())) {
            throw new ServiceException(new ServiceMessage("pass_change.new_password_equals_an_old_password"));
        }
        dmeAuthenticationService.changePassword(requestDto.getNewPassword(), requestDto.getOldPassword(), email);
        return new ServiceMessage("pass_change.password_successfully_changed");
    }
}


