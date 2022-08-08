package com.cargo.booking.auth.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("PassHash")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int tokenLifeTime;
}
