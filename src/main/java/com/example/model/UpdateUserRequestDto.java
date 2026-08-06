package com.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserRequestDto {
    
    @Email(message = "validation.email.invalid")
    private String email;
    
    @NotBlank(message = "validation.username.required")
    private String username;
}