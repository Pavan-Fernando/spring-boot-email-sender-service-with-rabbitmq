package com.example.notification.rabbitmq.backend.model.dto.external;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessageDto {
    private Long userId;
    @NotBlank(message = "email cannot be null")
    private String email;
    @NotBlank(message = "subject cannot be null")
    private String subject;
    @NotBlank(message = "message cannot be null")
    private String message;
}
