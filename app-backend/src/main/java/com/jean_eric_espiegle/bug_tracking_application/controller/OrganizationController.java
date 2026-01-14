package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.AddMemberRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.MemberDto;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationSignupResponse;
import com.jean_eric_espiegle.bug_tracking_application.dto.OwnershipTransferRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.OrganizationDto;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import com.jean_eric_espiegle.bug_tracking_application.service.OrganizationService;
import com.jean_eric_espiegle.bug_tracking_application.service.OrganizationSignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    public OrganizationController(
            UserRepository userRepository,
            OrganizationService organizationService) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getOrganizations(Authentication authentication) {
        // Get the logged-in user
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map Organizations to DTO
        List<OrganizationDto> orgDtos = user.getOrganizations().stream()
                .map(org -> new OrganizationDto(
                        org.getId(),
                        org.getName()))
                .toList();

        return ResponseEntity.ok(orgDtos);
    }

    @GetMapping("/{organizationId}/members")
    public ResponseEntity<List<MemberDto>> getMembers(
            @PathVariable Long organizationId,
            @RequestParam(value = "search", required = false) String searchTerm) {
        List<MemberDto> members = organizationService.getMembers(organizationId, searchTerm);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/signup")
    public ResponseEntity<OrganizationSignupResponse> signup(
            @RequestBody OrganizationSignupRequest request,
            Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Organization organization = organizationService.createOrganization(request, user);

        OrganizationSignupResponse response = new OrganizationSignupResponse(
                organization.getId(),
                organization.getName(),
                organization.getSubscriptionPlan().getType().name(),
                user.getUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{organizationId}/transfer-ownership")
    public ResponseEntity<Void> transferOwnership(
            @PathVariable Long organizationId,
            @RequestBody OwnershipTransferRequest request,
            Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        organizationService.transferOwnership(organizationId, request.newOwnerUsername(), currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{organizationId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable Long organizationId,
            @RequestBody AddMemberRequest request,
            Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        organizationService.addMember(organizationId, request, currentUser);
        return ResponseEntity.ok().build();
    }
}
