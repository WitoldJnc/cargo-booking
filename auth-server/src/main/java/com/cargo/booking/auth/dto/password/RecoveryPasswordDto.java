package com.cargo.booking.auth.dto.password;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class RecoveryPasswordDto {
    @Email(regexp = ".+[@].+[\\.].+", message = "{invalid_email}")
    @NotEmpty(message = "{empty_email}")
    private String email;

    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,}$",
            message = "{invalid_password}"
    )
    @NotBlank(message = "{empty_password}")
    private String newPassword;

    @NotBlank(message = "{empty_verification_code}")
    private String verificationCode;
}
