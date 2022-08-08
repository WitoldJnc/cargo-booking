package com.cargo.booking.auth.dto.password;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RecoveryPasswordRequestDto {
    @Email(regexp = ".+[@].+[\\.].+", message = "{invalid_email}")
    @NotBlank(message = "{empty_email}")
    private String email;
}
