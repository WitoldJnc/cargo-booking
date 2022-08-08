package com.cargo.booking.messages.rest;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class RestMessage {
    private String status = "ok";
    private String message;
    private Integer code;
    private Date timestamp;

    public RestMessage fail() {
        this.status = "fail";
        return this;
    }
}
