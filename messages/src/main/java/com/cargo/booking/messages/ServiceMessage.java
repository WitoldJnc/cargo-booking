package com.cargo.booking.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceMessage {
    private String messageCode;
    private Integer code;
    private Object[] args;

    public ServiceMessage(String messageCode) {
        this.messageCode = messageCode;
    }

    public ServiceMessage(MessageCode messageCode){
        this.messageCode = messageCode.getStringCode();
        this.code = messageCode.getIntCode();
    }

    public ServiceMessage(String messageCode, Object... args) {
        this.messageCode = messageCode;
        this.args = args;
    }
}
