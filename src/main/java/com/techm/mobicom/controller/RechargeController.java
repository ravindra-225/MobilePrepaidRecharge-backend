package com.techm.mobicom.controller;

import com.techm.mobicom.dto.RechargeDTO;
import com.techm.mobicom.dto.RechargeRequestDTO;
import com.techm.mobicom.models.PaymentMode;
import com.techm.mobicom.service.EmailService;
import com.techm.mobicom.service.PaymentValidationService;
import com.techm.mobicom.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private PaymentValidationService paymentValidationService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/recharges")
    public ResponseEntity<String> createRecharge(@RequestBody RechargeRequestDTO request) {
        // Validate payment details
        paymentValidationService.validatePaymentDetails(request);

        // Validate payment mode
        PaymentMode paymentMode;
        try {
            paymentMode = PaymentMode.valueOf(request.getPaymentMode().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid payment mode. Supported modes: " + 
                Arrays.stream(PaymentMode.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }

        // Map RechargeRequestDTO to RechargeDTO
        RechargeDTO rechargeDTO = new RechargeDTO();
        rechargeDTO.setMobileNumber(request.getMobileNumber());
        rechargeDTO.setPlanId(request.getPlanId());
        rechargeDTO.setAmount(request.getAmount());
        rechargeDTO.setPaymentMode(paymentMode);
        rechargeDTO.setStatus(request.getStatus());

        // Process recharge
        RechargeService.RechargeResult result = rechargeService.createRecharge(rechargeDTO);

        // Send confirmation email
        try {
            emailService.sendRechargeConfirmationEmail(
                result.getUserEmail(),
                request, result.getPlan(),
                request.getTransactionId()
            );
        } catch (Exception e) {
            // Log email failure but don't fail the recharge
            System.err.println("Failed to send email: " + e.getMessage());
        }

        String transactionMessage = request.getTransactionId() != null 
            ? ". Transaction ID: " + request.getTransactionId() 
            : "";
        return ResponseEntity.ok(result.getMessage() + transactionMessage);
    }

    @GetMapping("/payment-modes")
    public ResponseEntity<List<String>> getPaymentModes() {
        List<String> paymentModes = Arrays.stream(PaymentMode.values())
            .map(Enum::name)
            .collect(Collectors.toList());
        return ResponseEntity.ok(paymentModes);
    }
}