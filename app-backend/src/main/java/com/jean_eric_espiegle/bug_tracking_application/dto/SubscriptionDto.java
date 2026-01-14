package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.PlanType;

public record SubscriptionDto(
    PlanType planType,
    int maxAdmins,
    int maxSupport,
    int maxUsers,
    String organizationName,
    Long organizationId
) {}
