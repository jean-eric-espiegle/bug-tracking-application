package com.jean_eric_espiegle.bug_tracking_application.dto;

public record RegisterRequest(
        String username,
        String email,
        String password) {
}
