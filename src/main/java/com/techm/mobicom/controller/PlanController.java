package com.techm.mobicom.controller;

import com.techm.mobicom.models.Plan;
import com.techm.mobicom.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, List<Plan>>> getCategorizedPlans() {
        Map<String, List<Plan>> categorizedPlans = planService.getCategorizedPlans();
        return ResponseEntity.ok(categorizedPlans);
    }
}