package com.cargo.booking.starter.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @Getter
    @Setter
    private String publicKey;
}
