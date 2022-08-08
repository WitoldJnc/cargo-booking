package com.cargo.booking.auth.mapper;

import com.cargo.booking.auth.dto.registration.NewUserDto;
import com.cargo.booking.auth.dto.registration.UserRegistrationDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    NewUserDto userRegistrationDtoToNewUserDto(UserRegistrationDto dto);
}
