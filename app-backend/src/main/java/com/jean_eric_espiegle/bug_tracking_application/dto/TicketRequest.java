package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.TicketStatus;

public record TicketRequest(
        String title,
        String description,
        String reporterUsername,
        Long assigneeId,
        TicketStatus status,
        Long versionId) {
}
