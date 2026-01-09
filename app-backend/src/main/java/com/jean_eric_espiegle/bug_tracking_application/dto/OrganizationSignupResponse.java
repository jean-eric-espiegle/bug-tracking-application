package com.jean_eric_espiegle.bug_tracking_application.dto;

public record OrganizationSignupResponse(
        Long organizationId,
        String organizationName,
        String planType,
        String adminUsername) {
}
