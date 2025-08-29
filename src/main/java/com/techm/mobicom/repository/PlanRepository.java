package com.techm.mobicom.repository;

import com.techm.mobicom.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByCategory(String category);
}