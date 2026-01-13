package com.jean_eric_espiegle.bug_tracking_application.dto;

import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import java.time.LocalDateTime;

public class LogActionResponse {

    private Long id;
    private Long userId;
    private Long organizationId;
    private Role role;
    private String action;
    private String itemSnapshot;
    private LocalDateTime createdAt;

    // getters and setters
}
