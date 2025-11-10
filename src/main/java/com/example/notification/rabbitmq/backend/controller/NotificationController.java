package com.example.notification.rabbitmq.backend.controller;

import com.example.notification.rabbitmq.backend.model.dto.external.NotificationMessageDto;
import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.repository.NotificationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<NotificationLog> getById(@PathVariable UUID id) {
        Optional<NotificationLog> opt = repository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/status/{status}")
    public List<NotificationLog> getByStatus(@PathVariable String status) {
        return repository.findByStatus(status.toUpperCase());
    }


    @PostMapping("/send-test")
    public ResponseEntity<String> sendTest(@Valid @RequestBody NotificationMessageDto dto) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(dto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
        return ResponseEntity.ok("Published to queue");
    }
}
