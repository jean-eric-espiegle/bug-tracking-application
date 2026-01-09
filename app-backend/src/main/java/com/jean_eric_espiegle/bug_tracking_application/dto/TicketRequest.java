package com.jean_eric_espiegle.bug_tracking_application.dto;

public record TicketRequest(
        String title,
        String description,
        String reporterUsername,
        Long assigneeId,
        Long versionId) {
}
