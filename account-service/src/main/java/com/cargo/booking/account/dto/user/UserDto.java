package com.cargo.booking.account.dto.user;

import com.cargo.booking.account.dto.participant.ParticipantUserDto;
import com.cargo.booking.account.model.SystemRoleName;
import lombok.Data;

import java.util.*;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private Boolean blocked;
    private Long activeNotificationCount;
    private List<ParticipantUserDto> participantUsers = new ArrayList<>();
    private Set<SystemRoleName> systemRoles = new HashSet<>();
    private Set<String> systemAuthorities = new HashSet<>();
}
