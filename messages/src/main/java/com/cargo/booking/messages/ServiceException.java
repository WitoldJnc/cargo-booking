package com.cargo.booking.messages;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final ServiceMessage serviceMessage;

    public ServiceException(ServiceMessage serviceMessage) {
        this.serviceMessage = serviceMessage;
    }
}
