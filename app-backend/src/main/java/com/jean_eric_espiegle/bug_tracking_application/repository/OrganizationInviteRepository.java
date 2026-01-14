package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.OrganizationInvite;
import com.jean_eric_espiegle.bug_tracking_application.model.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationInviteRepository extends JpaRepository<OrganizationInvite, Long> {

    List<OrganizationInvite> findByOrganizationAndStatus(Organization organization, InviteStatus status);

    Optional<OrganizationInvite> findByInvitedEmailAndOrganizationAndStatus(
            String invitedEmail,
            Organization organization,
            InviteStatus status);

    boolean existsByInvitedEmailAndOrganizationAndStatus(
            String invitedEmail,
            Organization organization,
            InviteStatus status);
}
