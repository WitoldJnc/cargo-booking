package com.cargo.booking.auth.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cargo.booking.auth.exception.InternalServerErrorException;
import com.cargo.booking.auth.exception.InvalidCredentialsException;
import com.cargo.booking.auth.exception.UnknownResponseCodeException;
import com.cargo.booking.auth.external.dto.AuthServerError;
import com.cargo.booking.auth.external.dto.AuthServerResponse;
import com.cargo.booking.auth.external.dto.AuthToken;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class AuthenticationService {

    public static final String LOG_TEMPLATE = "[{}] {}";
    private static final int INVALID_VERIFICATION_CODE_RESPONSE_CODE = 201;
    private static final int EMAIL_NOT_FOUND = 202;
    private static final int EXISTING_EMAIL_RESPONSE_CODE = 205;
    private static final int INVALID_OLD_PASSWORD = 204;
    private static final String GRANT_TYPE = "password";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${auth.token}")
    private String authToken;

    @Value("${auth.host}")
    private String authHost;

    public AuthenticationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AuthToken login(String username, String password) throws InvalidCredentialsException {
        HttpEntity<MultiValueMap<String, String>> request = createAuthRequestEntity(username, password);
        ResponseEntity<AuthToken> response = executePostRequest(authHost + "/api/token", request, AuthToken.class);
        return response.getBody();
    }

    public AuthToken refresh(String username, String refreshToken) {
        HttpEntity<MultiValueMap<String, String>> request = createAuthRequestEntity(username, refreshToken);
        ResponseEntity<AuthToken> response = executePostRequest(authHost + "/api/token", request, AuthToken.class);
        return response.getBody();
    }

    public void sendVerificationCode(String email) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(1);
        body.add("email", email);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, getCommonHeaders());
        AuthServerResponse response = executePostRequest(authHost + "/api/RegisterRequest", request, AuthServerResponse.class)
                .getBody();
        processResponse(response, email);
    }

    public void register(String email, String password, String verificationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(3);
        body.add("Email", email);
        body.add("Pass", password);
        body.add("VerifCode", verificationCode);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, getCommonHeaders());
        AuthServerResponse response = executePostRequest(authHost + "/api/RegisterUser", request, AuthServerResponse.class)
                .getBody();
        processResponse(response, email);
    }

    public void sendRecoveryCode(String email) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(1);
        body.add("email", email);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, getCommonHeaders());
        AuthServerResponse response = executePostRequest(authHost + "/api/PassRecoveryRequest", request, AuthServerResponse.class)
                .getBody();
        processResponse(response, email);
    }

    public void changePasswordByCode(String email, String newPassword, String verificationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(3);
        body.add("email", email);
        body.add("pass", newPassword);
        body.add("VerifCode", verificationCode);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, getCommonHeaders());
        AuthServerResponse response = executePostRequest(authHost + "/api/PassRecovery", request, AuthServerResponse.class)
                .getBody();
        processResponse(response, email);
    }

    public void changePassword(String newPassword, String oldPassword, String email) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(3);
        body.add("email", email);
        body.add("pass", newPassword);
        body.add("oldPass", oldPassword);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, getCommonHeaders());
        AuthServerResponse response = executePostRequest(authHost + "/api/PassChange", request, AuthServerResponse.class)
                .getBody();
        processResponse(response, email);
    }

    private void processResponse(AuthServerResponse response, String email) {
        if (response != null && !response.isSuccess()) {
            switch (response.getCode()) {
                case INVALID_VERIFICATION_CODE_RESPONSE_CODE -> {
                    throw new ServiceException(new ServiceMessage("invalid_verification_code"));
                }
                case EXISTING_EMAIL_RESPONSE_CODE -> {
                    throw new ServiceException(new ServiceMessage("register.email_exists"));
                }
                case EMAIL_NOT_FOUND -> {
                    throw new ServiceException(new ServiceMessage("email_not_found", email));
                }
                case INVALID_OLD_PASSWORD -> {
                    throw new ServiceException(new ServiceMessage("pass_change.invalid_old_password"));
                }
                default -> {
                    String message = "Unknown response code: " + response.getCode();
                    throw new UnknownResponseCodeException(message);
                }
            }
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createAuthRequestEntity(String username, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(3);
        body.add("grant_type", GRANT_TYPE);
        body.add("username", username);
        body.add("password", password);
        return new HttpEntity<>(body, getCommonHeaders());
    }

    private MultiValueMap<String, String> getCommonHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(1);
        headers.add("Authentication", authToken);
        return headers;
    }

    private <T> ResponseEntity<T> executePostRequest(String url, Object request, Class<T> responseClass) {
        try {
            return restTemplate.postForEntity(url, request, responseClass);
        } catch (HttpClientErrorException ex) {
            if (ex.getRawStatusCode() == 400) {
                try {
                    AuthServerError error = objectMapper.readValue(ex.getResponseBodyAsString(), AuthServerError.class);
                    throw new InvalidCredentialsException(error.getErrorDescription(), ex);
                } catch (JsonProcessingException e) {
                    String errorMessage = "Не удалось разобрать сообщение с ошибкой от сервиса аутентификации";
                    log.error(errorMessage, e);
                    throw new InternalServerErrorException(errorMessage, e);
                }
            } else {
                throw ex;
            }
        } catch (HttpServerErrorException ex) {
            String errorMessage = "Внутренняя ошибка сервера аутентификации";
            throw new InternalServerErrorException(errorMessage, ex);
        }
    }
}
