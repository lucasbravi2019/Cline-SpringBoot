package com.example.controller;

import com.example.service.UserService;
import com.example.model.CreateUserRequestDto;
import com.example.model.UserDto;
import com.example.model.UpdateUserRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<Long> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        Long userId = userService.createUser(request);
        return ResponseEntity.created(URI.create("/user/" + userId)).body(userId);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDto request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
