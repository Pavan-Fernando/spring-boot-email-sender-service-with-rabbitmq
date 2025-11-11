package com.example.notification.rabbitmq.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuccessResponseDto {

    private UUID emailId;
    private String receiver;
    private String emailStatus;
}
