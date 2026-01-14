package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.repository.*;
import com.jean_eric_espiegle.bug_tracking_application.dto.*;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubscriptionSelectionService {

    private final SubscriptionPlanRepository planRepository;
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public SubscriptionSelectionService(
            SubscriptionPlanRepository planRepository,
            OrganizationRepository organizationRepository,
            MembershipRepository membershipRepository,
            UserRepository userRepository) {

        this.planRepository = planRepository;
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    public SelectPlanResponse selectPlan(
            String username,
            SelectPlanRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        SubscriptionPlan plan = planRepository.findById(request.planType())
                .orElseThrow(() -> new IllegalStateException("Plan not found"));

        // Update user's account-level subscription plan
        user.setSubscriptionPlan(plan);
        userRepository.save(user);

        // Create organization associated to this user (via membership only)
        Organization org = new Organization();
        if (plan.getType() == PlanType.FREE) {
            org.setName(user.getUsername() + "'s Organization");
        } else {
            org.setName(request.organizationName());
        }

        // Save organization first to generate ID
        organizationRepository.save(org);

        // Handle Enterprise plan separately
        if (plan.getType() == PlanType.ENTERPRISE) {
            org.setEnterprisePending(true);
            // Already saved above
            return new SelectPlanResponse(
                    org.getId(),
                    org.getName(),
                    plan.getType().name(),
                    "PENDING_SALES");
        }

        // For non-enterprise plans, create membership
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setOrganization(org);
        membership.setRole(Role.OWNER);

        membershipRepository.save(membership);

        return new SelectPlanResponse(
                org.getId(),
                org.getName(),
                plan.getType().name(),
                "ACTIVE");
    }
}
