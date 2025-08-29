//package com.techm.mobicom.service;
//
//import com.techm.mobicom.dto.RechargeRequest;
//import com.techm.mobicom.exception.ValidationException;
//import com.techm.mobicom.models.Recharge;
//import com.techm.mobicom.models.User;
//import com.techm.mobicom.models.Plan;
//import com.techm.mobicom.repository.RechargeRepository;
//import com.techm.mobicom.repository.PlanRepository;
//import com.techm.mobicom.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class RechargeServiceTest {
//
//    @Mock
//    private RechargeRepository rechargeRepository;
//
//    @Mock
//    private PlanRepository planRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @InjectMocks
//    private RechargeService rechargeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateRecharge_Success() {
//        RechargeRequest request = new RechargeRequest();
//        request.setUserId(1L);
//        request.setPlanId(1L);
//        request.setAmount(100.0);
//        request.setPaymentMode("UPI");
//        request.setPaymentDetails("test@upi");
//
//        User user = new User();
//        user.setId(1L);
//        user.setMobileNumber("1234567890");
//        user.setEmail("test@example.com");
//        user.setName("Test User");
//
//        Plan plan = new Plan();
//        plan.setId(1L);
//        plan.setValidity(30);
//        plan.setPrice(100.0);
//        plan.setName("Test Plan");
//
//        Recharge recharge = new Recharge();
//        recharge.setUser(user);
//        recharge.setPlan(plan);
//        recharge.setMobileNumber("1234567890");
//        recharge.setRechargeDate(LocalDate.now());
//        recharge.setExpiryDate(LocalDate.now().plusDays(30));
//        recharge.setPaymentMode(Recharge.PaymentMode.UPI);
//        recharge.setPaymentDetails("test@upi");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//        when(rechargeRepository.save(any(Recharge.class))).thenReturn(recharge);
//        doNothing().when(mailSender).send(any());
//
//        Recharge result = rechargeService.createRecharge(request);
//
//        assertNotNull(result);
//        assertEquals("1234567890", result.getMobileNumber());
//        verify(rechargeRepository).save(any(Recharge.class));
//        verify(mailSender).send(any());
//    }
//
//    @Test
//    void testCreateRecharge_InvalidPaymentDetails() {
//        RechargeRequest request = new RechargeRequest();
//        request.setUserId(1L);
//        request.setPlanId(1L);
//        request.setAmount(100.0);
//        request.setPaymentMode("UPI");
//        request.setPaymentDetails("invalid");
//
//        User user = new User();
//        user.setId(1L);
//
//        Plan plan = new Plan();
//        plan.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//
//        assertThrows(ValidationException.class, () -> rechargeService.createRecharge(request));
//    }
//}