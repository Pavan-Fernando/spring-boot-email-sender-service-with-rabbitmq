package com.example.notification.rabbitmq.backend.service.impl;

import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.service.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;


    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendEmail(NotificationLog log) throws Exception {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
        helper.setTo(log.getEmail());
        helper.setSubject(log.getSubject());
        helper.setText(log.getMessage(), true);
        javaMailSender.send(msg);
    }
}
