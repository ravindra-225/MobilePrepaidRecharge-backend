package com.techm.mobicom.service;

import com.techm.mobicom.dto.RechargeDTO;
import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.models.Plan;
import com.techm.mobicom.models.Recharge;
import com.techm.mobicom.models.User;
import com.techm.mobicom.repository.PlanRepository;
import com.techm.mobicom.repository.RechargeRepository;
import com.techm.mobicom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    public RechargeResult createRecharge(RechargeDTO rechargeDTO) {
        if (rechargeDTO.getPaymentMode() == null) {
            throw new CustomException("Payment mode is required", HttpStatus.BAD_REQUEST);
        }

        Plan plan = planRepository.findById(rechargeDTO.getPlanId())
                .orElseThrow(() -> new CustomException("Plan not found", HttpStatus.BAD_REQUEST));
        if (!rechargeDTO.getAmount().equals(plan.getPrice())) {
            throw new CustomException(
                "Amount does not match plan price. Expected: " + plan.getPrice() + ", Provided: " + rechargeDTO.getAmount(),
                HttpStatus.BAD_REQUEST
            );
        }

        User user = userRepository.findByMobileNumber(rechargeDTO.getMobileNumber())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.BAD_REQUEST));

        Recharge recharge = new Recharge();
        recharge.setMobileNumber(rechargeDTO.getMobileNumber());
        recharge.setPlan(plan);
        recharge.setAmount(rechargeDTO.getAmount());
        recharge.setRechargeDate(LocalDateTime.now());
        recharge.setExpiryDate(LocalDateTime.now().plusDays(plan.getValidity()));
        recharge.setPaymentMode(rechargeDTO.getPaymentMode());
        recharge.setStatus("Success");
        

        rechargeRepository.save(recharge);
        return new RechargeResult("Recharge successful", user.getEmail(), plan, "success");
    }

    public static class RechargeResult {
        private String message;
        private String userEmail;
        private Plan plan;
        private String status;

        public RechargeResult(String message, String userEmail, Plan plan, String status) {
            this.message = message;
            this.userEmail = userEmail;
            this.plan = plan;
            this.status=status;
        }

        public String getMessage() {
            return message;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public Plan getPlan() {
            return plan;
        }

		public String getStatus() {
			return status;
		}
    }
}