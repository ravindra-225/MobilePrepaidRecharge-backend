package com.techm.mobicom.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class RechargeHistoryDTO {
    @NotNull(message = "Plan name cannot be null")
    @Size(min = 1, message = "Plan name must be provided")
    private String planName;

    @NotNull(message = "Plan description cannot be null")
    @Size(min = 1, message = "Plan description must be provided")
    private String planDescription;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull(message = "Recharge date cannot be null")
    private LocalDateTime rechargeDate;

    @NotNull(message = "Expiry date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiryDate;

    @NotNull(message = "Payment mode cannot be null")
    @Size(min = 1, message = "Payment mode must be provided")
    private String paymentMode;
    
    @NotNull(message = " Payment status cannot be null")
    private String status;
}