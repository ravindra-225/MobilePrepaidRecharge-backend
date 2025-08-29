package com.techm.mobicom.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class LoginDTO {
    @NotNull(message = "Username cannot be null")
    @Size(min = 1, message = "Username must be provided")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 1, message = "Password must be provided")
    private String password;
}