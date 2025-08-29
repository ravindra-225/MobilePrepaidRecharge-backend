package com.techm.mobicom.dto;

import lombok.Data;

@Data
public class RechargeRequestDTO {
    private String mobileNumber;
    private Long planId;
    private Double amount;
    private String paymentMode;
    private String transactionId;
    private PaymentDetails paymentDetails;
    private String status;

    @Data
    public static class PaymentDetails {
        private String upiId; // e.g., "user@upi"
        private String cardNumber; // e.g., "1234567890123456"
        private String cardExpiry; // e.g., "12/25"
        private String cardCvv; // e.g., "123"
        private String bankAccountNumber; // e.g., "123456789012"
        private String bankIfscCode; // e.g., "SBIN0001234"
    }
}