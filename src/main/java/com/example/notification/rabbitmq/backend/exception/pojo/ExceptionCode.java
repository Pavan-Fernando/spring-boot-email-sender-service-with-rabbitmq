package com.example.notification.rabbitmq.backend.exception.pojo;

import com.example.notification.rabbitmq.backend.utils.Constant;
import lombok.Getter;



@Getter
public enum ExceptionCode {

    SBT001(Constant.ERROR_TYPE_SERVER),
    SBT002(Constant.ERROR_TYPE_INVALID_INPUT), //No resource found exception occurs
    SBT003(Constant.ERROR_TYPE_INVALID_INPUT), //Method Argument Not Valid Exception; // Employee details deleting failed

    SBT004(Constant.ERROR_TYPE_SERVER), //EMail sending failed
    SBT005(Constant.ERROR_TYPE_SERVER), //adding failed for MB
    SBT006(Constant.ERROR_TYPE_SERVER), //Json processing error
    SBT007(Constant.ERROR_TYPE_INVALID_INPUT); //Email record not found with UUID



    private final String errorType;

    ExceptionCode(String type) {
        this.errorType = type;
    }

    public String getType() {
        return errorType;
    }
}

