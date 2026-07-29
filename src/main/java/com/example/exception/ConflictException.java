package com.example.exception;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(message, 409);
    }
}