package com.example.controller;

import com.example.advice.GlobalExceptionHandler;
import com.example.exception.NotFoundException;
import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;
import com.example.model.UpdateUserRequestDto;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;
    
    @Autowired
    private MessageSource messageSource;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new UserController(userService))
            .setControllerAdvice(new GlobalExceptionHandler(messageSource))
            .build();
    }

    @Test
    void createUser_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setActive(true);
        
        Long userId = 1L;
        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(userId);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$").value(userId));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("invalid-email");
        request.setUsername("testuser");
        request.setActive(true);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email: Email address is invalid"))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenUsernameIsNull() throws Exception {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("test@example.com");
        request.setUsername(null);
        request.setActive(true);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("username: Username is required"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()));
    }
    
    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() throws Exception {
        Long userId = 1L;
        
        UserDto userDto = UserDto.builder()
            .id(userId)
            .email("test@example.com")
            .username("testuser")
            .build();
        
        when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
    
    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Long userId = 1L;
        
        when(userService.getUserById(userId)).thenThrow(new NotFoundException("validation.user.not.found", 1L));
        // Mock the behavior that should occur in real execution context
        mockMvc.perform(get("/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 1 not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()));
    }
    
    @Test
    void updateUser_ShouldUpdateUser() throws Exception {
        Long userId = 1L;
        
        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setEmail("updated@example.com");
        request.setUsername("updateduser");
                    
        mockMvc.perform(put("/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Long userId = 1L;
        
        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setEmail("updated@example.com");
        request.setUsername("updateduser");
        
        doThrow(new NotFoundException("validation.user.not.found", 1L)).when(userService)
            .updateUser(any(Long.class), any(UpdateUserRequestDto.class));

        mockMvc.perform(put("/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 1 not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()));
    }
    
    @Test
    void softDeleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        Long userId = 1L;
        
        mockMvc.perform(put("/user/{id}/delete", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void softDeleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Long userId = 1L;
        
        doThrow(new NotFoundException("validation.user.not.found", 1L)).when(userService).softDeleteUser(userId);

        // This is how you properly mock a void method to throw exception in Mockito  
        mockMvc.perform(put("/user/{id}/delete", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 1 not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()));
    }
}