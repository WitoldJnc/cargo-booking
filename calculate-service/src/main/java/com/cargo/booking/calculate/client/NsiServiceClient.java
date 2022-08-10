package com.cargo.booking.calculate.client;

import com.cargo.booking.calculate.dto.ScheduleRequestDto;
import com.cargo.booking.calculate.dto.nsi.RateInfoDto;
import com.cargo.booking.calculate.dto.nsi.RouteStatusDto;
import com.cargo.booking.calculate.dto.nsi.ScheduleDto;
import com.cargo.booking.calculate.exception.RemoteServiceUnavailableException;
import com.cargo.booking.messages.ServiceException;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient("${services.nsi}")
public interface NsiServiceClient {

    @Retry(name = "get-all-rate-by-arrival-and-departure-city-id", fallbackMethod = "getAllRateByArrivalAndDepartureCityIdFallback")
    @GetMapping(value = "/api/dictionary/rate/find/cities/{departure}/{arrival}")
    List<RateInfoDto> getAllRateByArrivalAndDepartureCityId(@PathVariable UUID departure, @PathVariable UUID arrival, @RequestParam String lang);

    default List<RateInfoDto> getAllRateByArrivalAndDepartureCityIdFallback(UUID departure, UUID arrival, String lang, Throwable ex) {
        if (ex instanceof ServiceException se) {
            throw se;
        } else {
            throw new RemoteServiceUnavailableException(ex);
        }
    }

    @Retry(name = "get-route-status-by-code", fallbackMethod = "getRouteStatusByCodeFallback")
    @Cacheable("nsi-route-status-by-code")
    @GetMapping("/api/dictionary/route-status/{code}")
    RouteStatusDto getRouteStatusByCode(@PathVariable String code, @RequestParam String lang);

    default RouteStatusDto getRouteStatusByCodeFallback(String code, String lang, Throwable ex) {
        if (ex instanceof ServiceException se) {
            throw se;
        } else {
            throw new RemoteServiceUnavailableException(ex);
        }
    }

    @Retry(name = "find-schedules", fallbackMethod = "findSchedulesFallback")
    @PostMapping("/internal/api/schedule/find/")
    List<ScheduleDto> findSchedules(@RequestBody ScheduleRequestDto scheduleFindRequestDto, @RequestParam String lang);

    default List<ScheduleDto> findSchedulesFallback(ScheduleRequestDto scheduleFindRequestDto, String lang, Throwable ex) {
        if (ex instanceof ServiceException se) {
            throw se;
        } else {
            throw new RemoteServiceUnavailableException(ex);
        }
    }
}
