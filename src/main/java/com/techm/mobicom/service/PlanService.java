package com.techm.mobicom.service;

import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.models.Plan;
import com.techm.mobicom.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @PostConstruct
    public void initDefaultPlans() {
        if (planRepository.count() == 0) {
            Plan plan1 = new Plan();
            plan1.setName("Basic Plan");
            plan1.setPrice(199.0);
            plan1.setValidity(28);
            plan1.setDescription("1.5GB/day, Unlimited Calls");
            plan1.setData("1.5GB/day");
            plan1.setCategory("POPULAR");

            Plan plan2 = new Plan();
            plan2.setName("Standard Plan");
            plan2.setPrice(399.0);
            plan2.setValidity(56);
            plan2.setDescription("2GB/day, Unlimited Calls, 100 SMS/day");
            plan2.setData("2GB/day");
            plan2.setCategory("VALIDITY");

            Plan plan3 = new Plan();
            plan3.setName("Premium Plan");
            plan3.setPrice(599.0);
            plan3.setValidity(84);
            plan3.setDescription("2.5GB/day, Unlimited Calls, Disney+ Hotstar");
            plan3.setData("2.5GB/day");
            plan3.setCategory("POPULAR");

            Plan plan4 = new Plan();
            plan4.setName("Data Only");
            plan4.setPrice(99.0);
            plan4.setValidity(28);
            plan4.setDescription("12GB Data, No Calls");
            plan4.setData("12GB");
            plan4.setCategory("DATA");

            Plan plan5 = new Plan();
            plan5.setName("Annual Plan");
            plan5.setPrice(2999.0);
            plan5.setValidity(365);
            plan5.setDescription("2GB/day, Unlimited Calls, Netflix Basic");
            plan5.setData("2GB/day");
            plan5.setCategory("VALIDITY");

            planRepository.saveAll(Arrays.asList(plan1, plan2, plan3, plan4, plan5));
        }
        
    }

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Map<String, List<Plan>> getCategorizedPlans() {
        List<String> categories = Arrays.asList("POPULAR", "VALIDITY", "DATA", "UNLIMITED_DATA");
        Map<String, List<Plan>> categorizedPlans = new HashMap<>();

        for (String category : categories) {
            List<Plan> plans = planRepository.findByCategory(category);
            if (!plans.isEmpty()) {
                categorizedPlans.put(category, plans);
            }
        }

        if (categorizedPlans.isEmpty()) {
            throw new CustomException("No plans available", HttpStatus.NOT_FOUND);
        }

        return categorizedPlans;
    }
}