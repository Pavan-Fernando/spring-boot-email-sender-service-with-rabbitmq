package com.example.notification.rabbitmq.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.notification.rabbitmq.backend.utils.Constant.*;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue notificationQueue() {
        // durable = true → survives broker restarts
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        // bind queue -> exchange using routing key
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(ROUTING_KEY);
    }
}

