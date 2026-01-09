package com.jean_eric_espiegle.bug_tracking_application.dto;

public record RegisterResponse(
        Long userId,
        String username,
        String email) {
}
