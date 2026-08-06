package com.example.service.impl;

import com.example.exception.NotFoundException;
import com.example.service.UserService;
import com.example.repository.UserRepository;
import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.model.UpdateUserRequestDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("validation.user.not.found", id)));
    }
    
    @Override
    public Long createUser(CreateUserRequestDto request) {
        // Validate request (already handled by Bean Validation)
        User user = userMapper.fromCreateRequest(request);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    
    @Override
    public void updateUser(Long id, UpdateUserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("validation.user.not.found", id));
        
        // Update fields from request
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("validation.user.not.found", id));
        
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
