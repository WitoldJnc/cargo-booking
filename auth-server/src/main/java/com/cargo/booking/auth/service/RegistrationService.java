package com.cargo.booking.auth.service;

import com.cargo.booking.auth.client.AccountServiceClient;
import com.cargo.booking.auth.dto.registration.UserRegistrationDto;
import com.cargo.booking.auth.external.AuthenticationService;
import com.cargo.booking.auth.mapper.UserMapper;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final String LOG_TEMPLATE = "[{}] {}";
    private final UserMapper userMapper;
    private final AccountServiceClient accountServiceClient;
    private final AuthenticationService dmeAuthenticationService;

    public ServiceMessage sendVerificationCodeTo(String email) {
        boolean emailExists = accountServiceClient.checkUserExistenceByEmail(email, LocaleContextHolder.getLocale().getLanguage());
        if (emailExists) {
            throw new ServiceException(new ServiceMessage("register.email_exists"));
        }
        dmeAuthenticationService.sendVerificationCode(email);
        return new ServiceMessage("register.verification_code_sent");
    }

    // TODO: потенциальная проблема: если не удалось создать пользователя в account-service,
    // то будет расхождение данных и повторно пользователь не сможет зарегистрироваться
    public ServiceMessage registerNewUser(UserRegistrationDto dto) {
        final String language = LocaleContextHolder.getLocale().getLanguage();
        boolean emailExists = accountServiceClient.checkUserExistenceByEmail(dto.getEmail(), language);
        if (emailExists) {
            throw new ServiceException(new ServiceMessage("register.email_exists"));
        }
        dmeAuthenticationService.register(dto.getEmail(), dto.getPassword(), dto.getVerificationCode());
        accountServiceClient.createNewUser(userMapper.userRegistrationDtoToNewUserDto(dto), language);
        return new ServiceMessage("register.user_is_registered", dto.getEmail());
    }
}
