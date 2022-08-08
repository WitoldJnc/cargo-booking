package com.cargo.booking.auth.dto;


import com.cargo.booking.auth.dto.user.UserDto;

public record Token(String username, String accessToken, String refreshToken, UserDto user) {}
