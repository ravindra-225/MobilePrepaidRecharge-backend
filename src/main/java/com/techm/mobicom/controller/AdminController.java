package com.techm.mobicom.controller;

import com.techm.mobicom.dto.RechargeHistoryDTO;
import com.techm.mobicom.dto.SubscriberExpiringDTO;
import com.techm.mobicom.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/subscribers/expiring")
    public ResponseEntity<List<SubscriberExpiringDTO>> getSubscribersWithExpiringPlans() {
        List<SubscriberExpiringDTO> subscribers = adminService.getSubscribersWithExpiringPlans();
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/subscribers/{mobileNumber}/rechargehistory")
    public ResponseEntity<List<RechargeHistoryDTO>> getUserRechargeHistory(@PathVariable String mobileNumber) {
        List<RechargeHistoryDTO> history = adminService.getUserRechargeHistory(mobileNumber);
        return ResponseEntity.ok(history);
    }
}