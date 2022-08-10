package com.cargo.booking.calculate.service;

import com.cargo.booking.calculate.client.AccountServiceClient;
import com.cargo.booking.calculate.client.NsiServiceClient;
import com.cargo.booking.calculate.dto.*;
import com.cargo.booking.calculate.dto.nsi.RateInfoDto;
import com.cargo.booking.calculate.dto.nsi.ScheduleDto;
import com.cargo.booking.calculate.dto.participant.CompanyDto;
import com.cargo.booking.calculate.dto.participant.ParticipantInfoDto;
import com.cargo.booking.calculate.exception.RatePriceNotPresentException;
import com.cargo.booking.calculate.exception.WeightLessThanMinimumException;
import com.cargo.booking.calculate.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferCalcService {

    private static final String BY_REQUEST = "BY_REQUEST";
    private static final String AUTO = "AUTO";
    private static final Integer CARGO_COEFFICIENT = 167;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final NsiServiceClient nsiServiceClient;
    private final AccountServiceClient accountServiceClient;
    private final DtoMapper dtoMapper;

    public List<OfferDto> findOfferRoutesByMinimal(OfferRequestMinimalDto request) {
        final String language = LocaleContextHolder.getLocale().getLanguage();
        List<RateInfoDto> rates = nsiServiceClient.getAllRateByArrivalAndDepartureCityId(request.getDepartureId(),
                request.getArrivalId(),
                language);
        List<OfferDto> offersByRate = new ArrayList<>();

        for (RateInfoDto rate : rates) {
            ParticipantInfoDto participantInfo = accountServiceClient.getParticipantInfo(rate.getParticipantId(), language);
            ScheduleRequestDto scheduleRequest = dtoMapper.offerRequestDtoToScheduleRequestDto(request);

            scheduleRequest.setAllowedAirlines(participantInfo.getSettings().getCargoAgentAirlines());
            scheduleRequest.setDayOfWeek(String.valueOf(request.getDepartureDate().getDayOfWeek().getValue()));
            List<ScheduleDto> schedules = nsiServiceClient.findSchedules(scheduleRequest, language);

            Double shippingCost;
            if (request.getWeight() == null) {
                request.setWeight(String.valueOf(rate.getMinWeight().doubleValue()));
            }
            shippingCost = calcShippingCostByRate(Double.valueOf(request.getWeight()), rate);

            schedules.forEach(schedule -> offersByRate.add(calcOfferDto(participantInfo.getCompany(), schedule, rate, request, shippingCost)));
        }
        return offersByRate;
    }

    public List<OfferDto> findOfferRoutesByFull(OfferRequestFullDto request) {
        final String language = LocaleContextHolder.getLocale().getLanguage();
        List<RateInfoDto> rates = nsiServiceClient.getAllRateByArrivalAndDepartureCityId(request.getDepartureId(),
                request.getArrivalId(),
                language);
        List<OfferDto> offersByRate = new ArrayList<>();

        for (RateInfoDto rate : rates) {
            ParticipantInfoDto participantInfo = accountServiceClient.getParticipantInfo(rate.getParticipantId(), language);
            ScheduleRequestDto scheduleRequest = dtoMapper.offerRequestDtoToScheduleRequestDto(request);

            scheduleRequest.setAllowedAirlines(participantInfo.getSettings().getCargoAgentAirlines());
            scheduleRequest.setDayOfWeek(String.valueOf(request.getDepartureDate().getDayOfWeek().getValue()));
            List<ScheduleDto> schedules = nsiServiceClient.findSchedules(scheduleRequest, language);

            double volumeWeight = 0.0;
            if ((request.getImp() != null && rate.getImpCode() != null) || (request.getImp() == null && rate.getImpCode() == null)) {
                for (CargoPartDto partDto : request.getCargoParts()) {
                    double coefficientVolume = Double.parseDouble(partDto.getVolume()) * CARGO_COEFFICIENT;
                    double maxValue = Math.max(coefficientVolume, Double.parseDouble(partDto.getWeight()));
                    double cargoWeight = partDto.getSeatsCount() * maxValue;
                    if (cargoWeight < rate.getMinWeight()) {
                        cargoWeight = rate.getMinWeight();
                    }
                    volumeWeight += cargoWeight;
                }
            }

            double shippingCost = calcShippingCostByRate(volumeWeight, rate);

            for (ScheduleDto schedule : schedules) {
                offersByRate.add(calcOfferDto(participantInfo.getCompany(), schedule, rate, request, shippingCost));
            }
        }
        return offersByRate;
    }

    private OfferDto calcOfferDto(CompanyDto company, ScheduleDto schedule, RateInfoDto rate, OfferRequestDto request, Double shippingCost) {
        final String language = LocaleContextHolder.getLocale().getLanguage();

        OfferDto offer = new OfferDto();
        offer.setId(UUID.randomUUID());
        offer.setCompany(company);
        offer.setArrivalAirportCode(schedule.getArrivalStation());
        offer.setDepartureAirportCode(schedule.getDepartureStation());

        if (shippingCost == null || shippingCost == 0.0) {
            offer.setStatus(nsiServiceClient.getRouteStatusByCode(BY_REQUEST, language).getName());
        } else {
            offer.setShippingCost(DECIMAL_FORMAT.format(shippingCost));
            offer.setCurrencySymbol(rate.getCurrencySymbol());
            offer.setStatus(nsiServiceClient.getRouteStatusByCode(AUTO, language).getName());
        }

        LocalDateTime departureDateTime = LocalDateTime.parse(String.format("%sT%s", request.getDepartureDate(), schedule.getDepartureTimeAircraftTz()));
        LocalDateTime arrivalDateTime = LocalDateTime.parse(String.format("%sT%s", request.getDepartureDate(), schedule.getArrivalTimeAircraft()));

        offer.setArrivalDateTime(arrivalDateTime.truncatedTo(ChronoUnit.SECONDS));
        offer.setDepartureDateTime(departureDateTime.truncatedTo(ChronoUnit.SECONDS));
        offer.setTravelTime(calcTravelDuration(schedule.getDepartureTimeAircraftTz(), schedule.getArrivalTimeAircraft()));
        offer.setTransitStation(List.of(TransitStation.AVIA));
        offer.setShipmentName(request.getShipmentName());
        offer.setCreateDt(LocalDateTime.now());
        offer.setExpirationDt(LocalDateTime.now().plus(Long.parseLong(String.valueOf(24l)), ChronoUnit.HOURS));

        return offer;
    }

    private TravelTime calcTravelDuration(LocalTime departure, LocalTime arrival) {
        int hours = arrival.getHour() - departure.getHour();
        int minutes = arrival.getMinute() - departure.getMinute();
        return new TravelTime(hours, minutes);
    }

    private Double calcShippingCostByRate(Double weight, RateInfoDto rate) throws WeightLessThanMinimumException, RatePriceNotPresentException {
        Double shippingCost = 0.0;
        if (weight >= rate.getMinWeight() && weight <= 45) {
            shippingCost = weight * rate.getLessThan45Kg();
        } else if (weight >= 46 && weight <= 100) {
            shippingCost = weight * rate.getFrom46To100Kg();
        } else if (weight >= 101 && weight <= 300) {
            shippingCost = weight * rate.getFrom101To300Kg();
        } else if (weight >= 301 && weight <= 1000) {
            shippingCost = weight * rate.getFrom301To1000Kg();
        } else if (weight >= 1001) {
            shippingCost = weight * rate.getOver1001Kg();
        }
        shippingCost += rate.getAirbillCost();
        return shippingCost;
    }

}
