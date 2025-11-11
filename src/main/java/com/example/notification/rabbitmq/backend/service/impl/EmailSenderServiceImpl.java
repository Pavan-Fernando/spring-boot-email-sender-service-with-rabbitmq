package com.example.notification.rabbitmq.backend.service.impl;

import com.example.notification.rabbitmq.backend.exception.CustomException;
import com.example.notification.rabbitmq.backend.model.entity.NotificationLog;
import com.example.notification.rabbitmq.backend.service.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.example.notification.rabbitmq.backend.exception.pojo.ExceptionCode.SBT004;

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
        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    SBT004,
                    "Email sending failed with mail: " + log.getEmail()
            );
        }
    }
}
