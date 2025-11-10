package com.example.notification.rabbitmq.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    public static final String ERROR_TYPE_SERVER = "Internal server error";
    public static final String ERROR_TYPE_INVALID_INPUT = "Invalid input values";

    public static final String EXCHANGE_NAME = "notification.exchanges";
    public static final String QUEUE_NAME = "notification.exchanges";
    public static final String ROUTING_KEY = "notification_routing.key";
}
