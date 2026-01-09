package com.jean_eric_espiegle.bug_tracking_application.dto;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String reporterUsername,
        String assigneeUsername,
        Long organizationId,
        String organizationName,
        Long versionId) {
}
