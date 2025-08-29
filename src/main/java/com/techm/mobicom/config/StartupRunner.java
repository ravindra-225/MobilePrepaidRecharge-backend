package com.techm.mobicom.config;

import com.techm.mobicom.dto.SubscriberExpiringDTO;
import com.techm.mobicom.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Autowired
    private AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking for subscribers with plans expiring within 3 days...");
        List<SubscriberExpiringDTO> subscribers = adminService.getSubscribersWithExpiringPlans();
        if (subscribers.isEmpty()) {
            logger.info("No subscribers have plans expiring within the next 3 days.");
        } else {
            logger.info("Found {} subscribers with expiring plans:", subscribers.size());
            subscribers.forEach(subscriber -> logger.info(
                "Mobile: {}, Name: {}, Email: {}, Plan: {} ({}), Price: {}, Expiry: {}",
                subscriber.getMobileNumber(),
                subscriber.getName(),
                subscriber.getEmail(),
                subscriber.getPlanName(),
                subscriber.getPlanDescription(),
                subscriber.getPlanPrice(),
                subscriber.getExpiryDate()
            ));
        }
    }
}