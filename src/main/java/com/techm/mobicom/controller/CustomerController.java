package com.techm.mobicom.controller;

import com.techm.mobicom.dto.RechargeRequestDTO;
import com.techm.mobicom.models.Recharge;
import com.techm.mobicom.service.RechargeService;
import com.techm.mobicom.service.UserService;
import com.techm.mobicom.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techm.mobicom.dto.RechargeDTO;
import com.techm.mobicom.dto.RechargeHistoryDTO;
import com.techm.mobicom.models.PaymentMode;
import com.techm.mobicom.models.Plan;
import com.techm.mobicom.repository.PlanRepository;
import com.techm.mobicom.service.AdminService;
import com.techm.mobicom.service.EmailService;
import com.techm.mobicom.service.PaymentValidationService;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PaymentValidationService paymentValidationService;
    
    @Autowired  private UserService userService;
    
    @Autowired private AdminService adminService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/recharges")
    @PreAuthorize("hasRole('USER')")
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

        // Process recharge
        RechargeService.RechargeResult result = rechargeService.createRecharge(rechargeDTO);

        // Send confirmation email
        try {
            emailService.sendRechargeConfirmationEmail(
                result.getUserEmail(),
                request,
                result.getPlan(),
                request.getTransactionId()
            );
        } catch (Exception e) {
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

    @GetMapping("/plans/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long id) {
        Plan plan = planRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Plan not found"));
        return ResponseEntity.ok(plan);
    }
    
    @GetMapping("/{mobileNumber}/rechargehistory")
    public ResponseEntity<List<RechargeHistoryDTO>> getUserRechargeHistory(@PathVariable String mobileNumber) {
        List<RechargeHistoryDTO> history = adminService.getUserRechargeHistory(mobileNumber);
        return ResponseEntity.ok(history);
    }
}