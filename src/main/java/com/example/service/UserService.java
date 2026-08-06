package com.example.service;

import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;
import com.example.model.UpdateUserRequestDto;

public interface UserService {
    UserDto getUserById(Long id);
    Long createUser(CreateUserRequestDto request);
    void updateUser(Long id, UpdateUserRequestDto request);
    void softDeleteUser(Long id);
}
