package com.example.notification.rabbitmq.backend.exception;

import com.example.notification.rabbitmq.backend.exception.pojo.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends Exception {

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String errorType;
    private final String errorMessage;

    public CustomException(HttpStatus statusCode,
                           ExceptionCode errorCode,
                           String errorMessage) {
        this.statusCode = statusCode;
        this.errorCode = errorCode.name();
        this.errorType = errorCode.getType();
        this.errorMessage = errorMessage;
    }

    public CustomException(HttpStatus statusCode,
                           ExceptionCode errorCode,
                           String errorMessage,
                           String logMessage) {
        super(logMessage);
        this.statusCode = statusCode;
        this.errorCode = errorCode.name();
        this.errorType = errorCode.getType();
        this.errorMessage = errorMessage;
    }

    public CustomException(HttpStatus statusCode,
                           ExceptionCode errorCode,
                           String errorMessage,
                           String logMessage,
                           Throwable e) {
        super(logMessage, e);
        this.statusCode = statusCode;
        this.errorCode = errorCode.name();
        this.errorType = errorCode.getType();
        this.errorMessage = errorMessage;
    }

    public CustomException(HttpStatus statusCode,
                           String errorCode,
                           String errorType,
                           String errorMessage,
                           String logMessage,
                           Throwable e) {
        super(logMessage, e);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }
}
