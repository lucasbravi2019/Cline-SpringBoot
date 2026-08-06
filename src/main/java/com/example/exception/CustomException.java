package com.example.exception;

import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    
    private final String messageKey;
    private final Object[] args;

    public CustomException(String message) {
        super(message);
        this.messageKey = Strings.EMPTY;
        this.args = new Object[]{};
    }

    public CustomException(String messageKey, Object[] args) {
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getLocalizedMessage(MessageSource messageSource) {
        if (this.messageKey == null)
            return super.getMessage();
        return messageSource.getMessage(this.messageKey, args, LocaleContextHolder.getLocale());
    }

}
