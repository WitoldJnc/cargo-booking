package com.cargo.booking.calculate.mapper;

import com.cargo.booking.calculate.dto.OfferRequestDto;
import com.cargo.booking.calculate.dto.ScheduleRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface DtoMapper {

    ScheduleRequestDto offerRequestDtoToScheduleRequestDto(OfferRequestDto offerRequestDto);
}
