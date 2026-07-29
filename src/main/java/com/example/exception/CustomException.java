package com.example.exception;

import lombok.Getter;
import lombok.Setter;

public class CustomException extends RuntimeException {
    @Getter
    @Setter
    private int statusCode;

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
