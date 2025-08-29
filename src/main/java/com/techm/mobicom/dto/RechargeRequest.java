package com.techm.mobicom.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RechargeRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Plan ID is required")
    private Long planId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "^(UPI|Bank Transfer|Credit/Debit Card)$", message = "Invalid payment mode")
    private String paymentMode;

    @NotBlank(message = "Payment details are required")
    private String paymentDetails;
}