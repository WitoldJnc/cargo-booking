package com.cargo.booking.calculate.dto.setting;

import lombok.Data;

import java.util.UUID;

@Data
public class SystemSettingDto {
    private UUID id;
    private String description;
    private String value;
}
