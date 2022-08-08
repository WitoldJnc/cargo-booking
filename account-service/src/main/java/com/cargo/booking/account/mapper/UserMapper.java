package com.cargo.booking.account.mapper;

import com.cargo.booking.account.dto.WorkspaceRoleDto;
import com.cargo.booking.account.dto.participant.ParticipantDto;
import com.cargo.booking.account.dto.participant.ParticipantUserDto;
import com.cargo.booking.account.dto.participant.ShortParticipantDto;
import com.cargo.booking.account.dto.user.NewUserDto;
import com.cargo.booking.account.dto.user.UserDto;
import com.cargo.booking.account.dto.user.UserInfoDto;
import com.cargo.booking.account.model.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(uses = ParticipantMapper.class)
public abstract class UserMapper {

    @Autowired
    private ParticipantMapper participantMapper;

    @Mappings({
            @Mapping(target = "participantUsers", ignore = true),
            @Mapping(target = "systemRoles", ignore = true),
            @Mapping(target = "systemAuthorities", ignore = true)
    })
    public abstract UserDto userToUserDto(User user);

    public abstract UserInfoDto userToUserInfoDto(User user);

    public List<UserInfoDto> usersToUserInfoDtos(List<User> users) {
        return users.stream().map(this::userToUserInfoDto).toList();
    }

    @Mappings(
            @Mapping(target = "blocked", constant = "false")
    )
    public abstract User newUserDtoToUser(NewUserDto newUserDto);

    @AfterMapping
    protected void afterUserToUserDto(@MappingTarget UserDto userDto, User user) {
        user.getParticipantUsers().forEach(pu -> {
            ParticipantDto pDto = participantMapper.participantToParticipantDto(pu.getParticipant());

            ParticipantUserDto puDto = new ParticipantUserDto();
            puDto.setAdministrator(pu.getAdministrator());
            puDto.setParticipant(pDto);

            pu.getWorkspaceRoles().forEach(wr -> {
                WorkspaceRoleDto wrDto = new WorkspaceRoleDto();
                wrDto.setId(wr.getId());
                wrDto.setWorkspaceId(wr.getWorkspace().getId());
                wrDto.setWorkspaceName(wr.getWorkspace().getName());
                wrDto.setRoleId(wr.getRole().getId());
                wrDto.setRoleName(wr.getRole().getName());
                wr.getAuthorities().forEach(auth -> wrDto.getAuthorities().add(auth.getName().name()));
                puDto.getWorkspaceRoles().add(wrDto);
            });

            userDto.getParticipantUsers().add(puDto);
        });

        Set<SystemRoleName> systemRoles = new HashSet<>();
        Set<String> systemAuthorities = new HashSet<>();
        user.getSystemRoles().forEach(sr -> {
            systemRoles.add(sr.getName());
            systemAuthorities.addAll(sr.getAuthorities().stream()
                    .map(authority -> authority.getName().name())
                    .collect(Collectors.toSet()));
        });
        userDto.getSystemRoles().addAll(systemRoles);
        userDto.getSystemAuthorities().addAll(systemAuthorities);
    }

  
}
