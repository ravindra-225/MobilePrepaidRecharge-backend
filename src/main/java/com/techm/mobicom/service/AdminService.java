package com.techm.mobicom.service;

import com.techm.mobicom.dto.RechargeHistoryDTO;
import com.techm.mobicom.dto.SubscriberExpiringDTO;
import com.techm.mobicom.models.Recharge;
import com.techm.mobicom.models.User;
import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.repository.RechargeRepository;
import com.techm.mobicom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SubscriberExpiringDTO> getSubscribersWithExpiringPlans() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysFromNow = now.plusDays(3);

        List<Recharge> expiringRecharges = rechargeRepository.findByExpiryDateBetween(now, threeDaysFromNow);

        if (expiringRecharges.isEmpty()) {
            return List.of();
        }

        return expiringRecharges.stream().map(recharge -> {
            User user = userRepository.findByMobileNumber(recharge.getMobileNumber())
                    .orElseThrow(() -> new CustomException("User not found for mobile: " + recharge.getMobileNumber(), HttpStatus.NOT_FOUND));

            SubscriberExpiringDTO dto = new SubscriberExpiringDTO();
            dto.setMobileNumber(recharge.getMobileNumber());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPlanName(recharge.getPlan().getName());
            dto.setPlanDescription(recharge.getPlan().getDescription());
            dto.setPlanPrice(recharge.getPlan().getPrice());
            dto.setExpiryDate(recharge.getExpiryDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<RechargeHistoryDTO> getUserRechargeHistory(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new CustomException("Invalid mobile number; must be 10 digits", HttpStatus.BAD_REQUEST);
        }
        userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException("User not found for mobile: " + mobileNumber, HttpStatus.NOT_FOUND));

        List<Recharge> recharges = rechargeRepository.findByMobileNumber(mobileNumber);

        if (recharges.isEmpty()) {
            throw new CustomException("No recharge history found for mobile: " + mobileNumber, HttpStatus.NOT_FOUND);
        }

        return recharges.stream().map(recharge -> {
            RechargeHistoryDTO dto = new RechargeHistoryDTO();
            dto.setPlanName(recharge.getPlan().getName());
            dto.setPlanDescription(recharge.getPlan().getDescription());
            dto.setAmount(recharge.getAmount());
            dto.setRechargeDate(recharge.getRechargeDate());
            dto.setExpiryDate(recharge.getExpiryDate());
            dto.setPaymentMode(recharge.getPaymentMode().name());
            dto.setStatus(recharge.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}