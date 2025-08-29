package com.techm.mobicom.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class SubscriberExpiringDTO {
    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, message = "Name must be provided")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Plan name cannot be null")
    @Size(min = 1, message = "Plan name must be provided")
    private String planName;

    @NotNull(message = "Plan description cannot be null")
    @Size(min = 1, message = "Plan description must be provided")
    private String planDescription;

    @Positive(message = "Plan price must be positive")
    private double planPrice;

    @NotNull(message = "Expiry date cannot be null")
    private LocalDateTime expiryDate;
}