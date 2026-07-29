package com.example.exception;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}