package com.cargo.booking.nsi.mapper;

import com.cargo.booking.nsi.dto.RateInfoDto;
import com.cargo.booking.nsi.dto.RatePreviewDto;
import com.cargo.booking.nsi.model.Rate;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Optional;

@Mapper
public abstract class RateMapper {
    public abstract RatePreviewDto rateToRatePreviewDto(Rate rate);

    public abstract RateInfoDto rateToRateInfoDto(Rate rate);

    public List<RateInfoDto> ratesToRateInfoDtos(List<Rate> rates) {
        return rates.stream().map(this::rateToRateInfoDto).toList();
    }

    @AfterMapping
    protected void afterRateToRateInfoDto(@MappingTarget RateInfoDto dto, Rate rate) {
        dto.setArrivalAirportCode(rate.getArrivalId().getCode());
        dto.setDepartureAirportCode(rate.getDepartureId().getCode());
        dto.setArrival(rate.getArrivalId().getId());
        dto.setDeparture(rate.getDepartureId().getId());
        dto.setAirline(rate.getAirlineId().getId());
        dto.setCurrencySymbol(rate.getCurrencyId().getSymbol());
        Optional.ofNullable(rate.getImpId())
                .ifPresent(impl -> dto.setImpCode(impl.getCode()));
    }
}