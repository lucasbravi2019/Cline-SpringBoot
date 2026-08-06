package com.example.mapper;

import com.example.entity.User;
import com.example.model.UserDto;
import com.example.model.CreateUserRequestDto;
import com.example.model.UpdateUserRequestDto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public User fromDto(UserDto dto) {
        return User.builder()
            .id(dto.getId())
            .username(dto.getUsername())
            .email(dto.getEmail())
            .build();
    }
    
    public User fromCreateRequest(CreateUserRequestDto request) {
        return User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .active(request.getActive())
            .createdAt(LocalDateTime.now())
            .build();
    }
    
    public UserDto toDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
    
    public User updateFromRequest(User user, UpdateUserRequestDto request) {
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
    
    public User updateActiveField(User user, Boolean active) {
        user.setActive(active);
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
