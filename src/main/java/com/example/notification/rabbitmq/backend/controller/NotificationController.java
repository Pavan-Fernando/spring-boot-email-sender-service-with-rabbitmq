package com.example.notification.rabbitmq.backend.controller;

import com.example.notification.rabbitmq.backend.exception.CustomException;
import com.example.notification.rabbitmq.backend.model.dto.external.NotificationMessageDto;
import com.example.notification.rabbitmq.backend.model.dto.response.SuccessResponseDto;
import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.repository.NotificationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.notification.rabbitmq.backend.exception.pojo.ExceptionCode.*;
import static com.example.notification.rabbitmq.backend.utils.Constant.EXCHANGE_NAME;
import static com.example.notification.rabbitmq.backend.utils.Constant.ROUTING_KEY;


@RestController
@RequestMapping("/api/notifications")
@Validated
public class NotificationController {


    private final NotificationLogRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public NotificationController(NotificationLogRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping
    public List<NotificationLog> getAll() {
        return repository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> getById(@PathVariable UUID id) throws CustomException {
        NotificationLog opt = repository.findById(id).orElseThrow(() ->
                new CustomException(
                        HttpStatus.BAD_REQUEST,
                        SBT007,
                        "Email record not found with UUID: " + id
                )
        );
        return new ResponseEntity<>(new SuccessResponseDto(opt.getId(), opt.getEmail(), opt.getStatus()), HttpStatus.OK);
    }


    @PostMapping("/send-test")
    public ResponseEntity<SuccessResponseDto> sendTest(@Valid @RequestBody NotificationMessageDto dto) throws CustomException {
        SuccessResponseDto email;
        try {
            email = (SuccessResponseDto) rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, ROUTING_KEY, objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    SBT006,
                    "Payload converting failed: " + e.getMessage()
            );
        }
        return new ResponseEntity<>(email, HttpStatus.OK);
    }
}
