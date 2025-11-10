package com.example.notification.rabbitmq.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "notification_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "user_id")
    private Long userId;


    private String email;


    private String subject;


    @Column(columnDefinition = "text")
    private String message;


    private String status;


    private OffsetDateTime createdAt;


    private OffsetDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }


    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

}
