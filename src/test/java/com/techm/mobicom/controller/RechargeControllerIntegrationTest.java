//package com.techm.mobicom.controller;
//
//import com.techm.mobicom.dto.RechargeRequestDTO;
//import com.techm.mobicom.models.PaymentMode;
//import com.techm.mobicom.models.Plan;
//import com.techm.mobicom.models.User;
//import com.techm.mobicom.repository.PlanRepository;
//import com.techm.mobicom.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class RechargeControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PlanRepository planRepository;
//
//    private String jwtToken;
//
//    @BeforeEach
//    public void setup() {
//        // Register a user
//        User user = new User();
//        user.setUsername("testuser");
//        user.setMobileNumber("1234567890");
//        user.setPassword("$2a$10$..."); // BCrypt encoded password
//        user.setEmail("test@example.com");
//        user.setRole("USER");
//        userRepository.save(user);
//
//        // Create a plan
//        Plan plan = new Plan();
//        plan.setName("Test Plan");
//        plan.setPrice(199.0);
//        plan.setValidity(28);
//        planRepository.save(plan);
//
//        // Simulate login to get JWT
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
//        String loginRequest = "{\"username\":\"testuser\",\"password\":\"password\"}";
//        ResponseEntity<AuthController.AuthenticationResponse> loginResponse = restTemplate.postForEntity(
//            "/api/login", new HttpEntity<>(loginRequest, headers), AuthController.AuthenticationResponse.class);
//        jwtToken = loginResponse.getBody().getToken();
//    }
//
//    @Test
//    public void testCreateRecharge_Success() {
//        RechargeRequestDTO request = new RechargeRequestDTO();
//        request.setMobileNumber("1234567890");
//        request.setPlanId(1L);
//        request.setAmount(199.0);
//        request.setPaymentMode("UPI");
//        RechargeRequestDTO.PaymentDetails paymentDetails = new RechargeRequestDTO.PaymentDetails();
//        paymentDetails.setUpiId("test@upi");
//        request.setPaymentDetails(paymentDetails);
//        request.setTransactionId("test123");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(jwtToken);
//        HttpEntity<RechargeRequestDTO> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/recharges", entity, String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("Recharge successful"));
//    }
//}