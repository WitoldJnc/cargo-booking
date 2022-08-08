package com.cargo.booking.nsi.controller;

import com.cargo.booking.nsi.dto.RateInfoDto;
import com.cargo.booking.nsi.dto.RatePreviewDto;
import com.cargo.booking.nsi.model.Rate;
import com.cargo.booking.nsi.service.RateDictionaryService;
import com.cargo.booking.starter.auth.ParticipantAuthenticationToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/dictionary/rate")
@ComponentScan("com.cargo.booking.messages")
public class RateDictionaryController {
    private final RateDictionaryService rateDictionaryService;

    @ApiOperation(value = "Save .xlsx rate dictionary file", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(value = "/save", consumes = "multipart/form-data", produces = "application/json")
    public List<Rate> parseAndSaveDictionary(@RequestParam("file") MultipartFile file) {
        ParticipantAuthenticationToken authentication = (ParticipantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return rateDictionaryService.saveNewDictionaryRecordsByParticipant(file, authentication.getParticipant());
    }

    @ApiOperation(value = "Get all rate records by participant", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/find/all", produces = "application/json")
    public List<RatePreviewDto> findAllRateByParticipant() {
        ParticipantAuthenticationToken authentication = (ParticipantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return rateDictionaryService.findRateRecordsByParticipant(authentication.getParticipant());
    }

    @ApiOperation(value = "Get all active rates by departure and arrival cities id")
    @GetMapping("/find/cities/{departure}/{arrival}")
    public List<RateInfoDto> getAllRateByArrivalAndDepartureCityId(@PathVariable UUID departure,
                                                                   @PathVariable UUID arrival) {
        return rateDictionaryService.findRatesByCities(departure, arrival);
    }
}