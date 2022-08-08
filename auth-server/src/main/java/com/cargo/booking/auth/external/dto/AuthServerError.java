package com.cargo.booking.auth.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthServerError {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
