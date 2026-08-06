package com.example.service.impl;

import com.example.entity.User;
import com.example.exception.NotFoundException;
import com.example.mapper.UserMapper;
import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void createUser_ShouldSaveUserAndReturnId_WhenRequestIsValid() {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setActive(true);
        
        User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .username("testuser")
        .active(true)
        .createdAt(LocalDateTime.now())
        .build();
        
        when(userMapper.fromCreateRequest(any(CreateUserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(1L, result);
        verify(userMapper, times(1)).fromCreateRequest(any(CreateUserRequestDto.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        Long userId = 1L;
        
        User user = User.builder().build();
        
        UserDto userDto = UserDto.builder()
            .id(userId)
            .email("test@example.com")
            .username("testuser")
            .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(any(User.class));
    }
    
    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        Long userId = 1L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertNotNull(exception);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toDto(any(User.class));
    }
    
}
