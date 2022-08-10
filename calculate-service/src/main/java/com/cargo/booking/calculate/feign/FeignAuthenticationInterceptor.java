package com.cargo.booking.calculate.feign;

import dme.cargo.starter.auth.ParticipantAuthenticationToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthenticationInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof ParticipantAuthenticationToken token) {
            template.header(HttpHeaders.AUTHORIZATION, token.getAuthorizationToken());
        }
    }
}
