package com.example.notification.rabbitmq.backend.exception;

import com.example.notification.rabbitmq.backend.exception.pojo.ErrorResponse;
import com.example.notification.rabbitmq.backend.exception.pojo.ExceptionCode;
import com.example.notification.rabbitmq.backend.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException ex) {
        log.error("No resource found exception occurs: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ExceptionCode.SBT002.toString(),
                ExceptionCode.SBT002.getErrorType(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Method argument not valid exception occurs: {}", ex.getMessage());
        String message;
        FieldError fieldError = ex.getFieldError();
        if (fieldError != null){
            message = fieldError.getDefaultMessage();
        }else {
            message = ex.getMessage();
        }
        ErrorResponse errorResponse = new ErrorResponse(
                ExceptionCode.SBT003.toString(),
                ExceptionCode.SBT003.getErrorType(),
                message
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> hnbIRRExceptionHandler(CustomException ex) {
        log.error("HNBIRRException occurred. errorCode : {} , errorMessage : {}", ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(), ex.getErrorType(), ex.getErrorMessage()), ex.getStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected exception occurs.}% %{ERROR: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ExceptionCode.SBT001.toString(),
                ExceptionCode.SBT001.getErrorType(),
                Constant.ERROR_TYPE_SERVER);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
