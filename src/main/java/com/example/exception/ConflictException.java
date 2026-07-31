package com.example.exception;

public class ConflictException extends CustomException {
    public ConflictException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
