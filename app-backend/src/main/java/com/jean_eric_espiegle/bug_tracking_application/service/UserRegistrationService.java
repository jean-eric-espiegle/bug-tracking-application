package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.RegisterRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.RegisterResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.PlanType;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import com.jean_eric_espiegle.bug_tracking_application.model.SubscriptionPlan;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.repository.SubscriptionPlanRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public UserRegistrationService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SubscriptionPlanRepository subscriptionPlanRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }

        SubscriptionPlan defaultPlan = subscriptionPlanRepository.findById(PlanType.FREE)
                .orElseThrow(() -> new IllegalStateException("Default FREE subscription plan not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setSubscriptionPlan(defaultPlan);

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail());
    }
}
