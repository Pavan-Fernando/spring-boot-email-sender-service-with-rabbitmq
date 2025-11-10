package com.example.notification.rabbitmq.backend.service;

import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;

public interface EmailSenderService {
    void sendEmail(NotificationLog log) throws Exception;
}
