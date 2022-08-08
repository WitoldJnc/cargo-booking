package com.cargo.booking.claim.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String email;
    private String subject;
    private String message;
}
