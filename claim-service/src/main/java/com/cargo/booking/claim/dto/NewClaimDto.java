package com.cargo.booking.claim.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class NewClaimDto {
    @NotEmpty(message = "{claim.empty_name}")
    @Size(min = 5, max = 14, message = "{claim.invalid_name_size}")
    private String name;
}
