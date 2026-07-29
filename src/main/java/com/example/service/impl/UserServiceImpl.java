package com.example.service.impl;

import com.example.exception.NotFoundException;
import com.example.exception.BadRequestException;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public String getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("User ID must be a positive number");
        }
        if (id > 1000) {
            throw new NotFoundException("User with ID " + id + " not found");
        }
        return "User details for ID: " + id;
    }
}
