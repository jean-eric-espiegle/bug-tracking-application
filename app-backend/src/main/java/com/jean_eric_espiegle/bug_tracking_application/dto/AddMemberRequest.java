package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.Role;

public record AddMemberRequest(String username, Role role) {
}
