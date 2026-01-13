package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.VersionRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.VersionResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.Organization;
import com.jean_eric_espiegle.bug_tracking_application.model.Version;
import com.jean_eric_espiegle.bug_tracking_application.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter USER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    private LocalDateTime parseReleaseDate(String releaseDate) {
        if (releaseDate == null) {
            return null;
        }
        try {
            // First, try to parse as ISO_LOCAL_DATE_TIME
            return LocalDateTime.parse(releaseDate, formatter);
        } catch (DateTimeParseException e) {
            // If that fails, try to parse as dd-MM-yyyy and set time to start of day
            LocalDate localDate = LocalDate.parse(releaseDate, USER_DATE_FORMATTER);
            return localDate.atStartOfDay();
        }
    }

    // Create a new version for an organization
    public Version createVersion(VersionRequest versionRequest, Organization organization) {
        Version version = new Version();
        version.setVersionName(versionRequest.name());
        version.setOrganization(organization);
        if (versionRequest.releaseDate() != null) {
            version.setReleasedAt(parseReleaseDate(versionRequest.releaseDate()));
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
            existingVersion.setReleasedAt(parseReleaseDate(versionRequest.releaseDate()));
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
