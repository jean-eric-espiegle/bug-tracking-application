package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.model.LogAction;
import com.jean_eric_espiegle.bug_tracking_application.service.LogActionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogActionController {

    private final LogActionService logActionService;

    public LogActionController(LogActionService logActionService) {
        this.logActionService = logActionService;
    }

    @GetMapping
    public List<LogAction> getLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return logActionService.getLogs(userId, organizationId, role, action, startDate, endDate);
    }
}
