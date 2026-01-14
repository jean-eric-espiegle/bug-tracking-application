package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organization_invites")
public class OrganizationInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false)
    private String invitedEmail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role invitedRole = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status = InviteStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime invitedAt = LocalDateTime.now();

    private LocalDateTime acceptedAt;
    private LocalDateTime expiresAt;

    // Constructors
    public OrganizationInvite() {}

    public OrganizationInvite(Organization organization, String invitedEmail, User invitedBy, Role invitedRole) {
        this.organization = organization;
        this.invitedEmail = invitedEmail;
        this.invitedBy = invitedBy;
        this.invitedRole = invitedRole;
        this.expiresAt = LocalDateTime.now().plusDays(7); // 7 days expiry
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getInvitedEmail() {
        return invitedEmail;
    }

    public void setInvitedEmail(String invitedEmail) {
        this.invitedEmail = invitedEmail;
    }

    public User getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(User invitedBy) {
        this.invitedBy = invitedBy;
    }

    public Role getInvitedRole() {
        return invitedRole;
    }

    public void setInvitedRole(Role invitedRole) {
        this.invitedRole = invitedRole;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public LocalDateTime getInvitedAt() {
        return invitedAt;
    }

    public void setInvitedAt(LocalDateTime invitedAt) {
        this.invitedAt = invitedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
