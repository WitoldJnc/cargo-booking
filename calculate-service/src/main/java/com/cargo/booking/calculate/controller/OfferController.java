package com.cargo.booking.calculate.controller;

import com.cargo.booking.calculate.dto.OfferDto;
import com.cargo.booking.calculate.dto.OfferRequestFullDto;
import com.cargo.booking.calculate.dto.OfferRequestMinimalDto;
import com.cargo.booking.calculate.service.OfferCalcService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/calculate")
@RestController
@RequiredArgsConstructor
@ComponentScan("dme.cargo.messages")
public class OfferController {
    private final OfferCalcService routeCalcService;

    @ApiOperation(value = "Get offers by cities, date")
    @PostMapping(value = "/offers/find/minimal", produces = "application/json")
    public List<OfferDto> getOffersByMinimal(@Valid @RequestBody OfferRequestMinimalDto requestDto) {
        return routeCalcService.findOfferRoutesByMinimal(requestDto);
    }

    @ApiOperation(value = "Get offers by cities, date, weight, imp", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(value = "/offers/find/full", produces = "application/json")
    public List<OfferDto> getOffersByFull(@Valid @RequestBody OfferRequestFullDto requestDto) {
        return routeCalcService.findOfferRoutesByFull(requestDto);
    }
}