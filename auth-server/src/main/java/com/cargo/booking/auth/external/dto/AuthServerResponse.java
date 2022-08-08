package com.cargo.booking.auth.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthServerResponse {

    @JsonProperty("Success")
    private Integer success;

    @Getter
    @JsonProperty("Code")
    private Integer code;

    public boolean isSuccess() {
        return success != null && success == 1;
    }
}
