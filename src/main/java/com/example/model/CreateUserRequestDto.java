package com.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserRequestDto {
    
    @NotBlank(message = "validation.email.required")
    @Email(message = "validation.email.invalid")
    private String email;
    
    @NotBlank(message = "validation.username.required")
    private String username;
    
    @NotNull(message = "validation.user.active.required")
    private Boolean active = true;
    
}