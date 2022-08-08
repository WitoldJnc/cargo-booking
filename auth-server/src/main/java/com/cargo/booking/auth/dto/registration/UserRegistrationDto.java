package com.cargo.booking.auth.dto.registration;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "{register.empty_first_name}")
    private String firstName;

    @NotBlank(message = "{register.empty_last_name}")
    private String lastName;

    private String middleName;

    @Email(regexp = ".+[@].+[\\.].+", message = "{dme.invalid_email}")
    @NotEmpty(message = "{empty_email}")
    private String email;

    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,}$",
            message = "{invalid_password}"
    )
    @NotBlank(message = "{empty_password}")
    private String password;

    @NotBlank(message = "{empty_verification_code}")
    private String verificationCode;
}
