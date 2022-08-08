package com.cargo.booking.nsi.controller;

import com.cargo.booking.nsi.dto.AirlineDto;
import com.cargo.booking.nsi.dto.CityDto;
import com.cargo.booking.nsi.dto.CountryDto;
import com.cargo.booking.nsi.dto.ImpDto;
import com.cargo.booking.nsi.service.DictionaryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dictionary")
@ComponentScan("com.cargo.booking.messages")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @ApiOperation(value = "Get list of countries", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/country", produces = "application/json")
    public List<CountryDto> getCountries() {
        return dictionaryService.getCountries();
    }

    @ApiOperation(value = "Get country by id", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/country/{countryId}", produces = "application/json")
    public CountryDto getCountry(@PathVariable UUID countryId) {
        return dictionaryService.getCountry(countryId);
    }

    @ApiOperation(value = "Get list of airlines", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/airline", produces = "application/json")
    public List<AirlineDto> getAirlines() {
        return dictionaryService.getAirlines();
    }

    @ApiOperation(value = "Get cities by name")
    @GetMapping(value = "/city", produces = "application/json")
    public List<CityDto> getCityByName(@ApiParam(name = "name", value = "City name ignore case", example = "Mosc")
                                       @RequestParam String name) {
        return dictionaryService.getCitiesByNameLike(name);
    }

    @ApiOperation(value = "Get city by id")
    @GetMapping(value = "/city/{cityId}", produces = "application/json")
    public CityDto getCityByName(@ApiParam(name = "cityId", value = "City id")
                                       @PathVariable UUID cityId) {
        return dictionaryService.getCityById(cityId);
    }


    @ApiOperation(value = "Get imp dictionary records by id", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/imp/{id}", produces = "application/json")
    public ImpDto getImpById(@PathVariable UUID id) {
        return dictionaryService.getImpById(id);
    }

    @ApiOperation(value = "Get imp dictionary records by code or name", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/imp/by-code", produces = "application/json")
    public List<ImpDto> getImpByCodeOrName(@RequestParam(required = false, defaultValue = "") String code) {
        return dictionaryService.getImpByCodeOrName(code);
    }

}
