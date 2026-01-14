package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.AccountPlanDto;
import com.jean_eric_espiegle.bug_tracking_application.dto.MembershipDto;
import com.jean_eric_espiegle.bug_tracking_application.dto.RegisterRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.RegisterResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.SubscriptionPlan;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import com.jean_eric_espiegle.bug_tracking_application.security.JwtRequest;
import com.jean_eric_espiegle.bug_tracking_application.security.JwtResponse;
import com.jean_eric_espiegle.bug_tracking_application.security.JwtUtil;
import com.jean_eric_espiegle.bug_tracking_application.service.UserRegistrationService;
import com.jean_eric_espiegle.bug_tracking_application.audit.LoggableAction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserRegistrationService registrationService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            UserRegistrationService registrationService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    @LoggableAction(action = "User Login", entity = "User")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name());

        AccountPlanDto accountPlan = null;
        if (user.getSubscriptionPlan() != null) {
            SubscriptionPlan plan = user.getSubscriptionPlan();
            accountPlan = new AccountPlanDto(
                    plan.getType(),
                    plan.getMaxAdmins(),
                    plan.getMaxSupport(),
                    plan.getMaxUsers());
        }

        List<MembershipDto> memberships = user.getMemberships().stream()
                .map(membership -> new MembershipDto(
                        membership.getOrganization().getId(),
                        membership.getOrganization().getName(),
                        membership.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(token, "dummy-refresh-token", user.getMembershipStatus(), accountPlan, memberships));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        RegisterResponse response = registrationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @LoggableAction(action = "User Logout", entity = "User")
    public ResponseEntity<Void> logout(Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
