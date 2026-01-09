package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Membership;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByUser(User user);

    long countByOrganizationAndRole(Organization organization, Role role);
}
