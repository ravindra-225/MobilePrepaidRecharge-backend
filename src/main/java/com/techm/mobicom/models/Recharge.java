package com.techm.mobicom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recharge")
public class Recharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNumber;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    private Double amount;
    private LocalDateTime rechargeDate;
    private LocalDateTime expiryDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    @Column
    private String status;
}