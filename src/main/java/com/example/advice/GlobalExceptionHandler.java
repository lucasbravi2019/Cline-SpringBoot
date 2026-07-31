package com.example.advice;

import com.example.exception.BadRequestException;
import com.example.exception.ConflictException;
import com.example.exception.NotFoundException;
import com.example.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage(ex.getLocalizedMessage(messageSource));
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.name());
        errorResponse.setMessage(ex.getLocalizedMessage(messageSource));
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ConflictException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError(HttpStatus.CONFLICT.name());
        errorResponse.setMessage(ex.getLocalizedMessage(messageSource));
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setError("Bad Request");
        
        // For validation errors, we'll use centralized messages with better structure
        if (!errors.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            errors.forEach((field, defaultMessage) -> {
                // Try to get a more descriptive centralized message if possible
                // First, check if there's a specific field-based message
                String key = "validation." + field + ".invalid";
                String localizedMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                
                // If we don't find a specific field message, use the generic one
                if (localizedMessage.equals(key)) {
                    // Fallback to general error message
                    localizedMessage = messageSource.getMessage("validation.general.error", 
                        new Object[]{defaultMessage}, LocaleContextHolder.getLocale());
                }
                
                messageBuilder.append(field).append(": ").append(localizedMessage).append("; ");
            });
            
            // Remove trailing "; " if present
            String finalMessage = messageBuilder.toString();
            if (finalMessage.endsWith("; ")) {
                finalMessage = finalMessage.substring(0, finalMessage.length() - 2);
            }
            
            errorResponse.setMessage(finalMessage);
        } else {
            errorResponse.setMessage("Validation failed");
        }
        
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
