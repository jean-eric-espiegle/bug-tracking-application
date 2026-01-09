package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.VersionRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.VersionResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.repository.MembershipRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import com.jean_eric_espiegle.bug_tracking_application.service.OrganizationService;
import com.jean_eric_espiegle.bug_tracking_application.service.VersionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VersionController {

    private final VersionService versionService;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;
    private final MembershipRepository membershipRepository;

    public VersionController(
            VersionService versionService,
            UserRepository userRepository,
            OrganizationService organizationService,
            MembershipRepository membershipRepository) {
        this.versionService = versionService;
        this.userRepository = userRepository;
        this.organizationService = organizationService;
        this.membershipRepository = membershipRepository;
    }

    private void checkMembership(User user, Organization organization) {
        if (!membershipRepository.existsByUserAndOrganization(user, organization)) {
            throw new IllegalArgumentException("You are not a member of this organization.");
        }
    }

    private void checkMembership(User user, Long versionId) {
        Version version = versionService.getVersionById(versionId);
        checkMembership(user, version.getOrganization());
    }

    @PostMapping("/organizations/{orgId}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public VersionResponse createVersion(@PathVariable Long orgId, @RequestBody VersionRequest versionRequest) {
        User currentUser = getCurrentUser();
        Organization org = organizationService.getOrganizationById(orgId);
        checkMembership(currentUser, org);

        Version newVersion = versionService.createVersion(versionRequest, org);
        return versionService.toResponse(newVersion);
    }

    @GetMapping("/organizations/{orgId}/versions")
    public List<VersionResponse> getVersionsForOrg(@PathVariable Long orgId) {
        User currentUser = getCurrentUser();
        Organization org = organizationService.getOrganizationById(orgId);
        checkMembership(currentUser, org);

        List<Version> versions = versionService.getVersionsForOrganization(org);
        return versionService.toResponseList(versions);
    }

    @GetMapping("/versions/{versionId}")
    public VersionResponse getVersionById(@PathVariable Long versionId) {
        User currentUser = getCurrentUser();
        checkMembership(currentUser, versionId);
        return versionService.toResponse(versionService.getVersionById(versionId));
    }

    @PutMapping("/versions/{versionId}")
    public VersionResponse updateVersion(@PathVariable Long versionId, @RequestBody VersionRequest versionRequest) {
        User currentUser = getCurrentUser();
        checkMembership(currentUser, versionId);
        Version updatedVersion = versionService.updateVersion(versionId, versionRequest);
        return versionService.toResponse(updatedVersion);
    }

    @DeleteMapping("/versions/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVersion(@PathVariable Long versionId) {
        User currentUser = getCurrentUser();
        checkMembership(currentUser, versionId);
        versionService.deleteVersion(versionId);
    }

    // Helper: get the currently authenticated user
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }
}
