package com.cargo.booking.nsi.mapper;

import com.cargo.booking.nsi.dto.ScheduleDto;
import com.cargo.booking.nsi.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    @Mappings({
            @Mapping(target = "departureStation", source = "departureStation.code"),
            @Mapping(target = "arrivalStation", source = "arrivalStation.code")

    })
    ScheduleDto scheduleToScheduleDto(Schedule schedule);

    default List<ScheduleDto> schedulesToScheduleDtos(List<Schedule> schedules) {
        return schedules.stream().map(this::scheduleToScheduleDto).toList();
    }

    default LocalTime instantToLocalTime(Instant instant) {
        return instant == null ? null : LocalTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
