package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import com.jean_eric_espiegle.bug_tracking_application.service.OrganizationService;
import com.jean_eric_espiegle.bug_tracking_application.service.VersionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
public class VersionController {

    private final VersionService versionService;
    private final OrganizationService organizationService;
    private final UserRepository userRepository;

    public VersionController(
            VersionService versionService,
            OrganizationService organizationService,
            UserRepository userRepository) {
        this.versionService = versionService;
        this.organizationService = organizationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public Version createVersion(@RequestParam Long orgId, @RequestParam String versionName) {
        // For MVP: ensure user is from same org
        Object user = this.getCurrentUser();
        // if (!user.getOrganization().getId().equals(orgId)) {
        // throw new IllegalArgumentException("You can only create versions in your
        // organization");
        // }
        System.err.println("User: " + user);

        Organization org = organizationService.getOrganizationById(orgId);
        return versionService.createVersion(versionName, org);
    }

    @GetMapping("/org/{orgId}")
    public List<Version> getVersionsForOrg(@PathVariable Long orgId) {
        User currentUser = getCurrentUser();
        Organization org = organizationService.getOrganizationById(orgId);

        if (!org.getId().equals(currentUser.getOrganization().getId())) {
            throw new IllegalArgumentException("Cannot view versions of another organization");
        }

        return versionService.getVersionsForOrganization(org);
    }

    @DeleteMapping("/delete/{versionId}")
    public void deleteVersion(@PathVariable Long versionId) {
        User currentUser = getCurrentUser();
        Version version = versionService.getVersionById(versionId);

        if (!version.getOrganization().getId().equals(currentUser.getOrganization().getId())) {
            throw new IllegalArgumentException("Cannot delete version of another organization");
        }

        versionService.deleteVersion(versionId);
    }

    // Helper: get the currently authenticated user
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }
}
