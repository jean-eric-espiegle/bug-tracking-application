package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.SelectPlanRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.SelectPlanResponse;
import com.jean_eric_espiegle.bug_tracking_application.service.SubscriptionSelectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionSelectionService selectionService;

    public SubscriptionController(SubscriptionSelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @PostMapping("/select")
    public ResponseEntity<SelectPlanResponse> selectPlan(
            @RequestBody SelectPlanRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        SelectPlanResponse response = selectionService.selectPlan(username, request);

        return ResponseEntity.ok(response);
    }
}
