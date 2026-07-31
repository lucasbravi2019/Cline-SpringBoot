package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@SpringBootTest
public class TestMessageSource {
    
    @Autowired
    private MessageSource messageSource;

    @Test
    void testMessages() {
        String message = messageSource.getMessage("validation.user.not.found", new Object[]{1L}, LocaleContextHolder.getLocale());

        assertEquals("User with ID 1 not found", message);
    }

}
