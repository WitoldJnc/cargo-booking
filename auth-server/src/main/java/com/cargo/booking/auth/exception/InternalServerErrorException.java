package com.cargo.booking.auth.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
