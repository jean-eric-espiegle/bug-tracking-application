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

        user.setSubscriptionPlan(plan);
        userRepository.save(user);

        Organization org = new Organization();
        if (plan.getType() == PlanType.FREE) {
            org.setName(user.getUsername() + "'s Organization");
        } else {
            org.setName(request.organizationName());
        }

        organizationRepository.save(org);

        if (plan.getType() == PlanType.ENTERPRISE) {
            org.setEnterprisePending(true);
            return new SelectPlanResponse(
                    org.getId(),
                    org.getName(),
                    plan.getType().name(),
                    "PENDING_SALES");
        }

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
