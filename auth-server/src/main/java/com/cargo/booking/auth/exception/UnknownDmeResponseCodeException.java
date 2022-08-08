package com.cargo.booking.auth.exception;

public class UnknownDmeResponseCodeException extends RuntimeException {
    public UnknownDmeResponseCodeException(String message) {
        super(message);
    }
}
