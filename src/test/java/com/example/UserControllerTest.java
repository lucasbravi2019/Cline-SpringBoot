package com.example;

import com.example.controller.UserController;
import com.example.exception.NotFoundException;
import com.example.exception.BadRequestException;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        // Use full Spring context to ensure exception handling works properly
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void shouldReturnUserDetailsWhenValidIdIsProvided() throws Exception {
        // Given
        Long id = 1L;
        String expectedUser = "User details for ID: 1";

        // When & Then
        mockMvc.perform(get("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUser));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidIdIsProvided() throws Exception {
        // Given
        Long id = -1L;

        // When & Then - This should now throw BadRequestException and be handled by GlobalExceptionHandler
        mockMvc.perform(get("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid user ID: -1"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        // Given
        Long id = 1001L;

        // When & Then
        mockMvc.perform(get("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 1,001 not found"));
    }
}
