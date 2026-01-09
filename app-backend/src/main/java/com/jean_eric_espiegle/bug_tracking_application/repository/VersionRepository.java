package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    // Find all versions for a given organization
    List<Version> findByOrganization(Organization organization);

    // Optional: find version by name and organization
    Version findByVersionNameAndOrganization(String versionName, Organization organization);
}
