package com.example.notification.rabbitmq.backend.listener;

import com.example.notification.rabbitmq.backend.model.dto.external.NotificationMessageDto;
import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.repository.NotificationLogRepository;
import com.example.notification.rabbitmq.backend.service.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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
    public void receive(@Payload String payload) {
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


            repository.save(logEntity);


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
        } catch (Exception e) {
            log.error("Failed to process incoming message: {}", e.getMessage());
        }
    }
}
