package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {
}
