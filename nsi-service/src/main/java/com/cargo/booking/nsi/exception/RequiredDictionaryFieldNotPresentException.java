package com.cargo.booking.nsi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequiredDictionaryFieldNotPresentException extends RuntimeException {
    private String requiredField;
    private int rowNum;
}