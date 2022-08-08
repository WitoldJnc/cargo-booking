package com.cargo.booking.auth.dto.registration;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class VerificationCodeRequestDto {

    @Email(regexp = ".+[@].+[\\.].+", message = "{invalid_email}")
    @NotBlank(message = "{empty_email}")
    private String email;
}
