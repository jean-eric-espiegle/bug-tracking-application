package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.TicketStatus;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String reporterUsername,
        String assigneeUsername,
        TicketStatus status,
        Long organizationId,
        String organizationName,
        Long versionId) {
}
