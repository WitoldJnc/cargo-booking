package com.cargo.booking.account.controller.internal;

import com.cargo.booking.account.dto.user.NewUserDto;
import com.cargo.booking.account.dto.user.UserDto;
import com.cargo.booking.account.service.UserService;
import com.cargo.booking.account.dto.user.*;
import com.cargo.booking.account.model.UserStatus;
import com.cargo.booking.account.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/internal/api")
@ComponentScan("com.cargo.booking.messages")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/user", produces = "application/json")
    public Optional<UserDto> findUserByEmail(@RequestParam String email) {
        return userService.findUserBy(email);
    }

    @GetMapping("/user/exists")
    public boolean checkUserExistenceByEmail(@RequestParam String email) {
        return userService.checkUserExistenceByEmail(email);
    }

    @PostMapping("/user")
    public UserDto createNewUser(@RequestBody NewUserDto newUserDto) {
        return userService.createNewUser(newUserDto);
    }

   
}
