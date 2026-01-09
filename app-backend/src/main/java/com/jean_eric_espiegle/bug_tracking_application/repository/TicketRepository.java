package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Ticket;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Find tickets by organization entity
    List<Ticket> findByOrganization(Organization organization);

    // Find tickets by organization id
    List<Ticket> findByOrganizationId(Long organizationId);

    // Find tickets assigned to a specific user
    List<Ticket> findByAssignee(User assignee);

    // Find tickets reported by a specific user
    List<Ticket> findByReporter(User reporter);

    // Find tickets by version id
    List<Ticket> findByVersionId(Long versionId);
}
