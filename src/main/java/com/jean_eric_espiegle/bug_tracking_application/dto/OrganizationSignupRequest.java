package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.PlanType;

public record OrganizationSignupRequest(
        String organizationName,
        PlanType planType) {
}
