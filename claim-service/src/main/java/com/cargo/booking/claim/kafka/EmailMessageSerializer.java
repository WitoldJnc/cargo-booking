package com.cargo.booking.claim.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cargo.booking.claim.dto.notification.EmailMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class EmailMessageSerializer implements Serializer<EmailMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, EmailMessage emailMessage) {
        try {
            if (emailMessage == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(emailMessage);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to serialize EmailMessage to byte[]", e);
        }
    }
}
