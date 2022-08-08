package com.cargo.booking.messages.rest;

import com.cargo.booking.messages.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler {
    private static final String UNEXPECTED_ERROR = "messages.unexpected_exception";
    private static final String INVALID_INPUT_ERROR = "messages.invalid_input";
    private final RestMessageFormatter restMessageFormatter;

    public RestExceptionHandler(RestMessageFormatter restMessageFormatter) {
        this.restMessageFormatter = restMessageFormatter;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RestMessage> handleServiceException(ServiceException ex) {
        return new ResponseEntity<>(
                restMessageFormatter.restMessage((BindingResult) ex.getServiceMessage()).fail(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestMessage> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                restMessageFormatter.restMessage(ex.getBindingResult()).fail(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestMessage> handleMethodArgumentTypeMismatchEx(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(
                restMessageFormatter.restMessage(INVALID_INPUT_ERROR, ex.getName()).fail(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestMessage> handleExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return new ResponseEntity<>(
                restMessageFormatter.restMessage(UNEXPECTED_ERROR).fail(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
