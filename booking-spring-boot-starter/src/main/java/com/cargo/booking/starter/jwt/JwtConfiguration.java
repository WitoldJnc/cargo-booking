package com.cargo.booking.starter.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(JwtRequestFilter.class)
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

    private final JwtProperties jwtProperties;

    public JwtConfiguration(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils() {
        return new JwtUtils(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtils());
    }
}
