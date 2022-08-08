package com.cargo.booking.auth.dto.password;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ChangePasswordRequestDto {

    @NotBlank(message = "{empty_password}")
    private String oldPassword;

    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,}$",
            message = "{invalid_password}"
    )
    @NotBlank(message = "{empty_password}")
    private String newPassword;

}
