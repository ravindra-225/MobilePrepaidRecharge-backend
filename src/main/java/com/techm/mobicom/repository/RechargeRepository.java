package com.techm.mobicom.repository;

import com.techm.mobicom.models.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    List<Recharge> findByExpiryDateBetween(LocalDateTime start, LocalDateTime end);
    List<Recharge> findByMobileNumber(String mobileNumber);
}