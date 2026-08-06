package com.example.service;

import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;

public interface UserService {
    UserDto getUserById(Long id);
    Long createUser(CreateUserRequestDto request);
}
