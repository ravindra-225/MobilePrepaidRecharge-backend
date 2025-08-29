package com.techm.mobicom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    private String mobileNumber;
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}