package com.techm.mobicom.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class AdminDTO {
    @NotNull(message = "Username cannot be null")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min= 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, message = "Name must be provided")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
}
