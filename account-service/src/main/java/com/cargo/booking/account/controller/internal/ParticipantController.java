package com.cargo.booking.account.controller.internal;

import com.cargo.booking.account.dto.participant.*;
import com.cargo.booking.account.model.CompanySortField;
import com.cargo.booking.account.model.ParticipantStatus;
import com.cargo.booking.account.model.ParticipantType;
import com.cargo.booking.account.service.ParticipantService;
import com.cargo.booking.messages.rest.RestMessage;
import com.cargo.booking.messages.rest.RestMessageFormatter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@AllArgsConstructor
@RestController
@ComponentScan("com.cargo.booking.messages")
@RequestMapping("/internal/api/participant")
public class ParticipantController {

    private final ParticipantService participantService;
    private final RestMessageFormatter messageFormatter;

    @ApiOperation(value = "Create new participant", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(produces = "application/json")
    public RestMessage createParticipant(@Valid @RequestBody NewParticipantDto participantDto) {
        return messageFormatter.restMessage(participantService.createParticipant(participantDto));
    }

    @ApiOperation(value = "Delete participant", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @DeleteMapping(value = "/{participantId}", produces = "application/json")
    public RestMessage deleteParticipant(@ApiParam(value = "Participant id", required = true) @PathVariable UUID participantId) {
        return messageFormatter.restMessage(participantService.deleteParticipant(participantId));
    }

    @ApiOperation(value = "Restore participant", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(value = "/{participantId}/restore", produces = "application/json")
    public RestMessage restoreParticipant(@ApiParam(value = "Participant id", required = true) @PathVariable UUID participantId) {
        return messageFormatter.restMessage(participantService.restoreParticipant(participantId));
    }

    @ApiOperation(value = "Search participants", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/search", produces = "application/json")
    public Page<DetailedParticipantDto> searchParticipants(@ApiParam(name = "size", value = "The number of pages", defaultValue = "10", example = "10")
                                                           @RequestParam(value = "size", required = false, defaultValue = "10")
                                                           int size,
                                                           @ApiParam(name = "page", value = "The page number. Start from 0", defaultValue = "0", example = "0")
                                                           @RequestParam(value = "page", required = false, defaultValue = "0")
                                                           int page,
                                                           @ApiParam(name = "type", value = "Participant type")
                                                           @RequestParam(value = "type")
                                                           ParticipantType participantType,
                                                           @ApiParam(name = "status", value = "The participant status", defaultValue = "ALL", example = "ALL")
                                                           @RequestParam(value = "status", required = false)
                                                           ParticipantStatus participantStatus,
                                                           @ApiParam(name = "query", value = "Search query")
                                                           @RequestParam(value = "query", required = false, defaultValue = "")
                                                           String query,
                                                           @ApiParam(name = "sortBy", value = "Sort field", defaultValue = "SHORT_NAME", example = "SHORT_NAME")
                                                           @RequestParam(value = "sortBy", required = false, defaultValue = "SHORT_NAME")
                                                           CompanySortField sortBy,
                                                           @ApiParam(name = "sortDirection", value = "Sort direction", defaultValue = "ASC", example = "ASC")
                                                           @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC")
                                                           Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, String.valueOf(sortBy)));
        return participantService.searchParticipants(pageable, participantStatus, participantType, query);
    }

    @ApiOperation(value = "Get participant info", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/{participantId}", produces = "application/json")
    public ParticipantInfoDto getParticipantInfo(@ApiParam(name = "participantId", value = "Participant id")
                                                 @PathVariable UUID participantId) {
        return participantService.getParticipantInfo(participantId);
    }
}
