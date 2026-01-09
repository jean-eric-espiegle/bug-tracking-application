package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
import com.jean_eric_espiegle.bug_tracking_application.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationSignupService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final SubscriptionPlanRepository planRepository;
    private final MembershipRepository membershipRepository;

    public OrganizationSignupService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            SubscriptionPlanRepository planRepository,
            MembershipRepository membershipRepository) {

        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public OrganizationSignupResponse signup(Long userId, OrganizationSignupRequest request) {

        // 1️⃣ Load existing user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // 2️⃣ Validate plan
        PlanType planType = request.planType();

        SubscriptionPlan plan = planRepository.findById(planType)
                .orElseThrow(() -> new IllegalStateException("Plan not found"));

        // 3️⃣ Create organization
        Organization organization = new Organization();
        organization.setName(request.organizationName());
        organization.setSubscriptionPlan(plan);

        organizationRepository.save(organization);

        // 4️⃣ Create OWNER membership
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setOrganization(organization);
        membership.setRole(Role.OWNER);

        membershipRepository.save(membership);

        // 5️⃣ Response
        return new OrganizationSignupResponse(
                organization.getId(),
                organization.getName(),
                plan.getType().name(),
                user.getUsername());
    }
}
