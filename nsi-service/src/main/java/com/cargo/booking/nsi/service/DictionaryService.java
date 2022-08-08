package com.cargo.booking.nsi.service;

import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import com.cargo.booking.nsi.dto.*;
import com.cargo.booking.nsi.mapper.DictionaryMapper;
import com.cargo.booking.nsi.model.*;
import com.cargo.booking.nsi.repository.AirlineRepository;
import com.cargo.booking.nsi.repository.CityRepository;
import com.cargo.booking.nsi.repository.CountryRepository;
import com.cargo.booking.nsi.repository.ImpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryMapper dictionaryMapper;
    private final CountryRepository countryRepository;
    private final AirlineRepository airlineRepository;
    private final CityRepository cityRepository;
    private final ImpRepository impRepository;

    public List<CountryDto> getCountries() {
        return dictionaryMapper.countriesToCountriesDtos(countryRepository.findAll());
    }

    public CountryDto getCountry(UUID id) {
        Optional<Country> maybeCountry = countryRepository.findById(id);
        if (maybeCountry.isEmpty()) {
            throw new ServiceException(new ServiceMessage("nsi.non_existent_country"));
        }
        return dictionaryMapper.countryToCountryDto(maybeCountry.get());
    }

    public List<AirlineDto> getAirlines() {
        return dictionaryMapper.airlinesToAirlineDtos(airlineRepository.findAll());
    }

    public List<CityDto> getCitiesByNameLike(String cityName) {
        return dictionaryMapper.citiesToCityDtos(cityRepository.findAllByNameLikeIgnoreCase(cityName));
    }

    public CityDto getCityById(UUID cityId) {
        return dictionaryMapper.cityToCityDto(cityRepository.findById(cityId).orElseThrow(() -> {
            throw new ServiceException(new ServiceMessage("nsi.city_not_found", cityId));
        }));
    }

    public ImpDto getImpById(UUID id) {
        try {
            return dictionaryMapper.impToImpDto(impRepository.findById(id).get());
        } catch (Throwable e) {
            throw new ServiceException(new ServiceMessage("nsi.imp.not_found"));
        }
    }

    public List<ImpDto> getImpByCodeOrName(String code) {
        return impRepository.findAllByCodeOrName(code.toUpperCase())
                .stream().map(dictionaryMapper::impToImpDto).toList();

    }
}
