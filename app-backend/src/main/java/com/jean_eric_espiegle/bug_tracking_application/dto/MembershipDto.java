package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.Role;

public record MembershipDto(
        Long organizationId,
        String organizationName,
        Role membershipRole) {
}
