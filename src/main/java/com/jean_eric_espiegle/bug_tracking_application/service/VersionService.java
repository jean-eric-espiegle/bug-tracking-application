package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionService {

    private final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    // Create a new version for an organization
    public Version createVersion(String versionName, Organization organization) {
        Version version = new Version();
        version.setVersionName(versionName);
        version.setOrganization(organization);
        return versionRepository.save(version);
    }

    // Get all versions for an organization
    public List<Version> getVersionsForOrganization(Organization organization) {
        return versionRepository.findByOrganization(organization);
    }

    // Delete a version (optional: handle tickets separately)
    public void deleteVersion(Long versionId) {
        versionRepository.deleteById(versionId);
    }

    // Find version by ID
    public Version getVersionById(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
    }
}
