package com.cargo.booking.nsi.mapper;

import com.cargo.booking.nsi.dto.CountryDto;
import com.cargo.booking.nsi.dto.*;
import com.cargo.booking.nsi.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface DictionaryMapper {

    @Mappings(
            @Mapping(target = "name", source = "nameLocale")
    )
    CountryDto countryToCountryDto(Country country);

    default List<CountryDto> countriesToCountriesDtos(List<Country> countries) {
        return countries.stream().map(this::countryToCountryDto).toList();
    }

    @Mappings({
            @Mapping(target = "name", source = "nameLocale"),
            @Mapping(target = "code", source = "airlineCode")
    })
    AirlineDto airlineToAirlineDto(Airline airline);

    default List<AirlineDto> airlinesToAirlineDtos(List<Airline> airlines) {
        return airlines.stream().map(this::airlineToAirlineDto).toList();
    }

    @Mappings(
            @Mapping(target = "name", source = "nameLocale")
    )
    CityDto cityToCityDto(City city);

    default List<CityDto> citiesToCityDtos(List<City> cities) {
        return cities.stream().map(this::cityToCityDto).toList();
    }


    @Mappings({
            @Mapping(target = "name", source = "nameLocale"),
    })
    ImpDto impToImpDto(Imp imp);

}
