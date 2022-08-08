package com.cargo.booking.account.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoDto {
    private UUID id;
    private String middleName;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean blocked;
}
