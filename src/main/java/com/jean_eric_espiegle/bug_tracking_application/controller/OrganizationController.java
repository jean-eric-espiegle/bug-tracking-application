package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import com.jean_eric_espiegle.bug_tracking_application.service.OrganizationSignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationSignupService signupService;
    private final UserRepository userRepository;

    public OrganizationController(
            OrganizationSignupService signupService,
            UserRepository userRepository) {
        this.signupService = signupService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<OrganizationSignupResponse> signup(
            @RequestBody OrganizationSignupRequest request,
            Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                signupService.signup(user.getId(), request));
    }
}
