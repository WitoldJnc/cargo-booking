package com.cargo.booking.auth.client;

import com.cargo.booking.auth.dto.registration.NewUserDto;
import com.cargo.booking.auth.dto.user.UserDto;
import com.cargo.booking.auth.exception.RemoteServiceUnavailableException;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient("${services.account}")
public interface AccountServiceClient {

    @Retry(name = "find-user-by-email", fallbackMethod = "findUserByEmailFallback")
    @GetMapping("/internal/api/user")
    Optional<UserDto> findUserByEmail(@RequestParam String email, @RequestParam String lang);

    default Optional<UserDto> findUserByEmailFallback(String email, String lang, Throwable ex) {
        return Optional.empty();
    }

    @Retry(name = "check-user-existence-by-email", fallbackMethod = "checkUserExistenceByEmailFallback")
    @GetMapping("/internal/api/user/exists")
    boolean checkUserExistenceByEmail(@RequestParam String email, @RequestParam String lang);

    default boolean checkUserExistenceByEmailFallback(String email, String lang, Throwable ex) {
        throw new RemoteServiceUnavailableException(ex);
    }

    @Retry(name = "create-new-user", fallbackMethod = "createNewUserFallback")
    @PostMapping("/internal/api/user")
    UserDto createNewUser(@RequestBody NewUserDto dto, @RequestParam String lang);

    default UserDto createNewUserFallback(NewUserDto dto, String lang, Throwable ex) {
        throw new RemoteServiceUnavailableException(ex);
    }
}
