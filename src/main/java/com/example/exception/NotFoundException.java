package com.example.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
