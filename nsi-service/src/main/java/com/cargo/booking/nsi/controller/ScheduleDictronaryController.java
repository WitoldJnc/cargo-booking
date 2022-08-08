package com.cargo.booking.nsi.controller;

import com.cargo.booking.messages.rest.RestMessage;
import com.cargo.booking.messages.rest.RestMessageFormatter;
import com.cargo.booking.nsi.dto.ScheduleDto;
import com.cargo.booking.nsi.dto.ScheduleRequestDto;
import com.cargo.booking.nsi.service.ScheduleDirectoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/internal/api")
@ComponentScan("com.cargo.booking.messages")
public class ScheduleDictronaryController {

    private final ScheduleDirectoryService scheduleDirectoryService;
    private final RestMessageFormatter messageFormatter;

    @ApiOperation(value = "Add schedule", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(value = "/schedule/upload", consumes = "multipart/form-data", produces = "application/json")
    public RestMessage addSchedule(@RequestParam("file") MultipartFile file) {
        return messageFormatter.restMessage(scheduleDirectoryService.scheduleUpload(file));
    }

    @ApiOperation(value = "Get schedules by params")
    @PostMapping("/schedule/find/")
    public List<ScheduleDto> findSchedulesBy(@RequestBody ScheduleRequestDto scheduleFindRequestDto) {
        return scheduleDirectoryService.findSchedulesByCities(scheduleFindRequestDto);
    }

    @ApiOperation(value = "Get schedule by id", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping("/schedule/{scheduleId}")
    public ScheduleDto getScheduleById(@PathVariable UUID scheduleId) {
        return scheduleDirectoryService.getScheduleById(scheduleId);
    }
}
