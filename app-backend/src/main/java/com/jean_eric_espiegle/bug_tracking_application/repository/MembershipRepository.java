package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Membership;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByUserIdAndOrganizationId(
            Long userId,
            Long organizationId);

    Optional<Membership> findByUserAndOrganization(User user, Organization organization);

    boolean existsByUserAndOrganization(User user, Organization organization);

    long countByOrganizationAndRole(Organization organization, Role role);

    long countByOrganization(Organization organization);

    long countByUserAndRole(User user, Role role);

    List<Membership> findByOrganizationIdAndUserEmailContainingIgnoreCase(Long organizationId, String email);

    List<Membership> findByOrganizationId(Long organizationId);

    boolean existsByOrganizationIdAndUser(Long organizationId, User user);
}
