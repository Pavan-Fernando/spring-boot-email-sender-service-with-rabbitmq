package com.example.notification.rabbitmq.backend.listener;

import com.example.notification.rabbitmq.backend.exception.CustomException;
import com.example.notification.rabbitmq.backend.model.dto.external.NotificationMessageDto;
import com.example.notification.rabbitmq.backend.model.dto.response.SuccessResponseDto;
import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.repository.NotificationLogRepository;
import com.example.notification.rabbitmq.backend.service.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.example.notification.rabbitmq.backend.exception.pojo.ExceptionCode.SBT005;
import static com.example.notification.rabbitmq.backend.utils.Constant.EXCHANGE_NAME;


@Component
@Slf4j
public class NotificationListener {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationLogRepository repository;
    private final EmailSenderService emailSenderService;


    public NotificationListener(NotificationLogRepository repository, EmailSenderService emailSenderService) {
        this.repository = repository;
        this.emailSenderService = emailSenderService;
    }


    @RabbitListener(queues = EXCHANGE_NAME)
    @Transactional
    public SuccessResponseDto receive(@Payload String payload) throws CustomException {
        try {
            NotificationMessageDto dto = objectMapper.readValue(payload, NotificationMessageDto.class);
            log.info("Received notification message for userId={}", dto.getUserId());


            NotificationLog logEntity = NotificationLog.builder()
                    .userId(dto.getUserId())
                    .email(dto.getEmail())
                    .subject(dto.getSubject())
                    .message(dto.getMessage())
                    .status("PENDING")
                    .build();


            logEntity = repository.save(logEntity);


            try {
                emailSenderService.sendEmail(logEntity);
                logEntity.setStatus("SENT");
                repository.save(logEntity);
                log.info("Email sent for notification id={}", logEntity.getId());
            } catch (Exception e) {
                log.error("Failed to send email for notification id={}, error={}", logEntity.getId(), e.getMessage());
                logEntity.setStatus("FAILED");
                repository.save(logEntity);
            }
            return new SuccessResponseDto(logEntity.getId(), logEntity.getEmail(), logEntity.getStatus());
        } catch (Exception e) {
            log.error("Failed to process incoming message: {}", e.getMessage());
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    SBT005,
                    "Adding failed for message broker: " + e.getMessage()
            );
        }
    }
}
