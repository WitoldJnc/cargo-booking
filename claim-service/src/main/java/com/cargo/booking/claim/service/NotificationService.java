package com.cargo.booking.claim.service;

import com.cargo.booking.claim.dto.notification.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${kafka.notification.email.topic}")
    private String emailNotificationTopic;

    private final KafkaTemplate<String, EmailMessage> kafkaSender;

    public NotificationService(KafkaTemplate<String, EmailMessage> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    public void mailTo(String email, String subject, String message) {
        kafkaSender.send(emailNotificationTopic, new EmailMessage(email, subject, message));
    }
}
