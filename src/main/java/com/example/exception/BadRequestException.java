package com.example.exception;

public class BadRequestException extends CustomException {
    
    public BadRequestException(String messageKey, Object... args) {
        super(messageKey, args);
    }


}
