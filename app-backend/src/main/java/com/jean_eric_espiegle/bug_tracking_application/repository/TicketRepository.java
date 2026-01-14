package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Ticket;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByOrganization(Organization organization);

    List<Ticket> findByOrganizationId(Long organizationId);

    List<Ticket> findByAssignee(User assignee);

    List<Ticket> findByReporter(User reporter);

    List<Ticket> findByVersionId(Long versionId);
}
