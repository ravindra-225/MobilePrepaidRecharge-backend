package com.techm.mobicom.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UserLoginDTO {
    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;
}