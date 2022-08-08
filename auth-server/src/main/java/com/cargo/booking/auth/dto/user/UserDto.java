package com.cargo.booking.auth.dto.user;

import lombok.Data;

import java.util.*;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean blocked;
    private Long activeNotificationCount;
    private List<ParticipantUserDto> participantUsers = new ArrayList<>();
    private Set<String> systemRoles = new HashSet<>();
    private Set<String> systemAuthorities = new HashSet<>();
}
