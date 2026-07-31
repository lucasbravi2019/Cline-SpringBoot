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
            throw new BadRequestException("validation.user.invalid.id", id);
        }
        if (id > 1000) {
            throw new NotFoundException("validation.user.not.found", id);
        }
        return "User details for ID: " + id;
    }
}
