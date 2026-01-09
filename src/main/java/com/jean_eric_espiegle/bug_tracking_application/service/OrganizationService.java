package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Fetch an organization by its ID
     * 
     * @param id the organization ID
     * @return Organization entity
     * @throws IllegalStateException if not found
     */
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Organization not found with ID: " + id));
    }
}
