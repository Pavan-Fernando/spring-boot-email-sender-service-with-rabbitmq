package com.example.notification.rabbitmq.backend.model.dto.external;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessageDto {
    private Long userId;
    private String email;
    private String subject;
    private String message;
}
