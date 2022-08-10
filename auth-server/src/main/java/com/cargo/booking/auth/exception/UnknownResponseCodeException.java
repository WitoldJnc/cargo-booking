package com.cargo.booking.auth.exception;

public class UnknownResponseCodeException extends RuntimeException {
    public UnknownResponseCodeException(String message) {
        super(message);
    }
}
