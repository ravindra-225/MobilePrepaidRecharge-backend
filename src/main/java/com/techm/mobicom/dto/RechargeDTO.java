package com.techm.mobicom.dto;
import java.time.LocalDateTime;

import com.techm.mobicom.models.PaymentMode;
import lombok.Data;

@Data
public class RechargeDTO {
    private String mobileNumber;
    private Long planId;
    private Double amount;
    private PaymentMode paymentMode;
    private String status;
    private LocalDateTime rechargeDate;
}