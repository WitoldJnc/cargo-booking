package com.cargo.booking.account.dto.user;

import lombok.Data;

@Data
public class NewUserDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
}
