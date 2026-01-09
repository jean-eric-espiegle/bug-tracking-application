package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.SubscriptionPlan;
import com.jean_eric_espiegle.bug_tracking_application.model.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, PlanType> {
}
