package com.jean_eric_espiegle.bug_tracking_application.dto;

public record SelectPlanResponse(
        Long organizationId,
        String organizationName,
        String plan,
        String status) {
}
