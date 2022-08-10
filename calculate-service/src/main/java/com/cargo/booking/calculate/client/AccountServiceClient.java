package com.cargo.booking.calculate.client;

import com.cargo.booking.calculate.dto.participant.ParticipantInfoDto;
import com.cargo.booking.calculate.exception.RemoteServiceUnavailableException;
import com.cargo.booking.messages.ServiceException;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("${services.account}")
public interface AccountServiceClient {

    @Retry(name = "get-participant-info-by-id", fallbackMethod = "getParticipantInfoFallback")
    @GetMapping(value = "/internal/api/participant/{participantId}")
    ParticipantInfoDto getParticipantInfo(@PathVariable UUID participantId, @RequestParam String lang);

    default ParticipantInfoDto getParticipantInfoFallback(UUID participantId, String lang, Throwable ex) {
        if (ex instanceof ServiceException se) {
            throw se;
        } else {
            throw new RemoteServiceUnavailableException(ex);
        }
    }
}
