package com.cargo.booking.auth.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String username;
    private String refreshToken;
}
