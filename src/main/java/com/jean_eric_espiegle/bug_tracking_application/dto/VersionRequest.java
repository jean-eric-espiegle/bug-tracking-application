package com.jean_eric_espiegle.bug_tracking_application.dto;

public record VersionRequest(
        String name,
        String releaseDate // Could use String or LocalDate, depending on your preference
) {
}
