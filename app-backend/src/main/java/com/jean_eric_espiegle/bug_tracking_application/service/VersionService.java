package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.VersionRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.VersionResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    // Create a new version for an organization
    public Version createVersion(VersionRequest versionRequest, Organization organization) {
        Version version = new Version();
        version.setVersionName(versionRequest.name());
        version.setOrganization(organization);
        if (versionRequest.releaseDate() != null) {
            version.setReleasedAt(LocalDateTime.parse(versionRequest.releaseDate(), formatter));
        }
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

    public Version updateVersion(Long versionId, VersionRequest versionRequest) {
        Version existingVersion = getVersionById(versionId);
        existingVersion.setVersionName(versionRequest.name());
        if (versionRequest.releaseDate() != null) {
            existingVersion.setReleasedAt(LocalDateTime.parse(versionRequest.releaseDate(), formatter));
        } else {
            existingVersion.setReleasedAt(null);
        }
        return versionRepository.save(existingVersion);
    }

    public VersionResponse toResponse(Version version) {
        String releasedAt = version.getReleasedAt() != null ? formatter.format(version.getReleasedAt()) : null;
        return new VersionResponse(version.getId(), version.getVersionName(), releasedAt);
    }

    public List<VersionResponse> toResponseList(List<Version> versions) {
        return versions.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
