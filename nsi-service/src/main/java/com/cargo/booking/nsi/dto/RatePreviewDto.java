package com.cargo.booking.nsi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatePreviewDto {
    private String fileName;
    private Boolean isActive;
    private LocalDateTime dtUpload;
}