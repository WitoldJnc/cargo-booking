package com.cargo.booking.account.service;

import com.cargo.booking.account.dto.user.NewUserDto;
import com.cargo.booking.account.dto.user.UserDto;
import com.cargo.booking.account.mapper.ParticipantMapper;
import com.cargo.booking.account.mapper.UserMapper;
import com.cargo.booking.account.repository.jpa.SystemRoleRepository;
import com.cargo.booking.account.repository.jpa.UserRepository;
import com.cargo.booking.account.model.SystemRoleName;
import com.cargo.booking.account.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private static final String USER_NOT_FOUND = "account.user_not_found";

    private final UserMapper userMapper;
    private final ParticipantMapper participantMapper;
    private final UserRepository userRepository;
    private final SystemRoleRepository systemRoleRepository;

    @Transactional(readOnly = true)
    public Optional<UserDto> findUserBy(String email) {
        Optional<User> maybeUser = userRepository.findByEmailIgnoreCase(email);
        return maybeUser.map(userMapper::userToUserDto);
    }

    public boolean checkUserExistenceByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDto createNewUser(NewUserDto newUserDto) {
        User newUser = userMapper.newUserDtoToUser(newUserDto);
        newUser.getSystemRoles().add(systemRoleRepository.getByName(SystemRoleName.USER));
        return userMapper.userToUserDto(userRepository.save(newUser));
    }

}
