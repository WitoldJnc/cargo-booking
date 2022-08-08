package com.cargo.booking.auth.dto.registration;

import lombok.Data;

@Data
public class NewUserDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
}
