package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.AddMemberByEmailRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.AddMemberRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.CreateOrganizationRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.MemberDto;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupRequest;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
import com.jean_eric_espiegle.bug_tracking_application.repository.MembershipRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.OrganizationInviteRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.OrganizationRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.SubscriptionPlanRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jean_eric_espiegle.bug_tracking_application.audit.LoggableAction;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

import com.jean_eric_espiegle.bug_tracking_application.audit.LoggableAction;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final OrganizationInviteRepository inviteRepository;

    public OrganizationService(OrganizationRepository organizationRepository, MembershipRepository membershipRepository,
            UserRepository userRepository, SubscriptionPlanRepository planRepository,
            OrganizationInviteRepository inviteRepository) {
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.inviteRepository = inviteRepository;
    }

    @Transactional
    public Organization createOrganization(OrganizationSignupRequest request, User user) {
        PlanType planType = request.planType();

        if (planType == PlanType.FREE) {
            long ownedOrganizations = membershipRepository.countByUserAndRole(user, Role.OWNER);
            if (ownedOrganizations > 0) {
                throw new IllegalStateException("Users on the FREE plan can only own one organization.");
            }
        }

        SubscriptionPlan plan = planRepository.findById(planType)
                .orElseThrow(() -> new IllegalStateException("Plan not found"));

        Organization organization = new Organization();
        organization.setName(request.organizationName());

        organizationRepository.save(organization);

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setOrganization(organization);
        membership.setRole(Role.OWNER);

        membershipRepository.save(membership);

        return organization;
    }

    @Transactional
    public Organization createOrganizationWithAutoPlan(CreateOrganizationRequest request, User user) {
        long ownedOrganizations = membershipRepository.countByUserAndRole(user, Role.OWNER);

        PlanType planType = PlanType.FREE;

        SubscriptionPlan plan = planRepository.findById(planType)
                .orElseThrow(() -> new IllegalStateException("Default plan not found"));

        Organization organization = new Organization();
        organization.setName(request.organizationName());

        organizationRepository.save(organization);

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setOrganization(organization);
        membership.setRole(Role.OWNER);

        membershipRepository.save(membership);

        return organization;
    }

    public List<MemberDto> getMembers(Long organizationId, String searchTerm) {
        List<Membership> memberships;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            memberships = membershipRepository.findByOrganizationId(organizationId);
        } else {
            memberships = membershipRepository.findByOrganizationIdAndUserEmailContainingIgnoreCase(organizationId,
                    searchTerm);
        }

        return memberships.stream()
                .map(membership -> new MemberDto(
                        membership.getUser().getId(),
                        membership.getUser().getUsername(),
                        membership.getUser().getEmail(),
                        membership.getRole()))
                .collect(Collectors.toList());
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Organization not found with ID: " + id));
    }

    public boolean isMember(Long organizationId, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }
        return membershipRepository.existsByOrganizationIdAndUser(organizationId, user);
    }

    @LoggableAction(action = "Transferred Ownership", entity = "Organizations")
    @Transactional
    public void transferOwnership(Long organizationId, String newOwnerUsername, User currentUser) {
        Organization organization = getOrganizationById(organizationId);

        Membership currentOwnerMembership = findMembership(currentUser, organization);
        if (currentOwnerMembership.getRole() != Role.OWNER) {
            throw new IllegalStateException("Only the owner can transfer ownership.");
        }

        User newOwner = userRepository.findByUsername(newOwnerUsername)
                .orElseThrow(() -> new IllegalStateException("User not found: " + newOwnerUsername));

        Membership newOwnerMembership = findMembership(newOwner, organization);

        currentOwnerMembership.setRole(Role.ADMIN);
        newOwnerMembership.setRole(Role.OWNER);

        membershipRepository.save(currentOwnerMembership);
        membershipRepository.save(newOwnerMembership);
    }

    private Membership findMembership(User user, Organization organization) {
        return membershipRepository.findByUserAndOrganization(user, organization)
                .orElseThrow(() -> new IllegalStateException(
                        "User " + user.getUsername() + " is not a member of organization " + organization.getName()));
    }

    @LoggableAction(action = "Added New Member", entity = "Organizations")
    @Transactional
    public void addMember(Long organizationId, AddMemberRequest request, User currentUser) {
        Organization organization = getOrganizationById(organizationId);

        Membership currentUserMembership = findMembership(currentUser, organization);
        if (currentUserMembership.getRole() != Role.OWNER && currentUserMembership.getRole() != Role.ADMIN) {
            throw new IllegalStateException("You do not have permission to add members to this organization.");
        }

        User newUser = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("User not found: " + request.username()));

        if (membershipRepository.existsByUserAndOrganization(newUser, organization)) {
            throw new IllegalStateException("User is already a member of this organization.");
        }

        checkPlanLimits(organization, request.role());

        Membership newMembership = new Membership();
        newMembership.setUser(newUser);
        newMembership.setOrganization(organization);
        newMembership.setRole(request.role());

        membershipRepository.save(newMembership);
    }

    @LoggableAction(action = "Invited New Member", entity = "Organizations")
    @Transactional
    public void inviteMember(Long organizationId, AddMemberByEmailRequest request, User currentUser) {
        Organization organization = getOrganizationById(organizationId);

        Membership currentUserMembership = findMembership(currentUser, organization);
        if (currentUserMembership.getRole() != Role.OWNER && currentUserMembership.getRole() != Role.ADMIN) {
            throw new IllegalStateException("You do not have permission to invite members to this organization.");
        }

        String email = request.email().toLowerCase().trim();

        if (inviteRepository.existsByInvitedEmailAndOrganizationAndStatus(email, organization, InviteStatus.PENDING)) {
            throw new IllegalStateException("An invitation has already been sent to this email address.");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User newUser = existingUser.get();

            if (membershipRepository.existsByUserAndOrganization(newUser, organization)) {
                throw new IllegalStateException("User is already a member of this organization.");
            }

            checkPlanLimits(organization, request.role());

            Membership membership = new Membership();
            membership.setUser(newUser);
            membership.setOrganization(organization);
            membership.setRole(request.role());

            membershipRepository.save(membership);
        } else {
            OrganizationInvite invite = new OrganizationInvite(
                    organization,
                    email,
                    currentUser,
                    request.role());

            inviteRepository.save(invite);
        }
    }

    @LoggableAction(action = "Deleted Organization", entity = "Organization")
    @Transactional
    public void deleteOrganization(Long organizationId, User currentUser) {
        Organization organization = getOrganizationById(organizationId);

        Membership currentUserMembership = findMembership(currentUser, organization);
        if (currentUserMembership.getRole() != Role.OWNER) {
            throw new IllegalStateException(
                    "Only the organization owner can delete the organization. You do not have permission to delete this organization.");
        }

        List<Membership> memberships = membershipRepository.findByOrganizationId(organizationId);

        long ownerCount = memberships.stream()
                .filter(m -> m.getRole() == Role.OWNER)
                .count();

        if (ownerCount > 1) {
            throw new IllegalStateException(
                    "Cannot delete organization with multiple owners. Please transfer ownership to a single owner first.");
        }

        membershipRepository.deleteAll(memberships);

        organizationRepository.delete(organization);
    }

    private void checkPlanLimits(Organization organization, Role newRole) {
        User owner = organization.getMemberships().stream()
                .filter(membership -> membership.getRole() == Role.OWNER)
                .findFirst()
                .map(Membership::getUser)
                .orElseThrow(() -> new IllegalStateException("Organization has no owner"));

        SubscriptionPlan plan = owner.getSubscriptionPlan();
        if (plan == null) {
            plan = planRepository.findById(PlanType.FREE)
                    .orElseThrow(() -> new IllegalStateException("Default FREE plan not found"));
        }

        long totalUsers = membershipRepository.countByOrganization(organization);
        if (totalUsers >= plan.getMaxUsers()) {
            throw new IllegalStateException(
                    "Cannot add new member. Organization has reached its maximum user limit for the " + plan.getType()
                            + " plan.");
        }

        if (newRole == Role.ADMIN) {
            long adminCount = membershipRepository.countByOrganizationAndRole(organization, Role.ADMIN);
            if (adminCount >= plan.getMaxAdmins()) {
                throw new IllegalStateException(
                        "Cannot add new admin. Organization has reached its maximum admin limit for the "
                                + plan.getType() + " plan.");
            }
        }

        if (newRole == Role.SUPPORT) {
            long supportCount = membershipRepository.countByOrganizationAndRole(organization, Role.SUPPORT);
            if (supportCount >= plan.getMaxSupport()) {
                throw new IllegalStateException(
                        "Cannot add new support member. Organization has reached its maximum support limit for the "
                                + plan.getType() + " plan.");
            }
        }
    }
}
