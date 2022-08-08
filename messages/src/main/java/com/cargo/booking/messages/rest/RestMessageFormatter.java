package com.cargo.booking.messages.rest;

import com.cargo.booking.messages.ServiceMessage;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.stream.Collectors;


@Component
public class RestMessageFormatter {
    private final MessageSource messageSource;

    public RestMessageFormatter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public RestMessage restMessage(String messageCode, Object... args) {
        RestMessage restMessage = new RestMessage();
        restMessage.setMessage(messageSource.getMessage(
                messageCode, args, messageCode, LocaleContextHolder.getLocale()
        ));
        restMessage.setTimestamp(new Date());

        return restMessage;
    }

    public RestMessage restMessage(ServiceMessage serviceMessage) {
        RestMessage message = restMessage(serviceMessage.getMessageCode(), serviceMessage.getArgs());
        message.setCode(serviceMessage.getCode());

        return message;
    }

    public RestMessage restMessage(BindingResult bindingResult) {
        RestMessage restMessage = new RestMessage();
        restMessage.setMessage(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                .collect(Collectors.joining("; ")));
        restMessage.setTimestamp(new Date());

        return restMessage;
    }
}
