package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.LogActionResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.LogAction;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import com.jean_eric_espiegle.bug_tracking_application.repository.LogActionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogActionService {

    private final LogActionRepository logActionRepository;

    public LogActionService(LogActionRepository logActionRepository) {
        this.logActionRepository = logActionRepository;
    }

    public List<LogActionResponse> getLogs(
            Long userId,
            Long organizationId,
            String roleStr,
            String action,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        // Build JPA Specification
        Specification<LogAction> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }

        if (organizationId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("organization").get("id"), organizationId));
        }

        if (roleStr != null) {
            Role roleEnum = Role.valueOf(roleStr.toUpperCase());
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), roleEnum));
        }

        if (action != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("action"), "%" + action + "%"));
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
        }

        // Fetch logs sorted by creation date descending
        List<LogAction> logs = logActionRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Map entities to DTOs
        return logs.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private LogActionResponse mapToDto(LogAction logAction) {
        LogActionResponse dto = new LogActionResponse();
        dto.setId(logAction.getId());
        if (logAction.getUser() != null) {
            dto.setUserId(logAction.getUser().getId());
            dto.setUsername(logAction.getUser().getUsername());
        }
        if (logAction.getOrganization() != null) {
            dto.setOrganizationId(logAction.getOrganization().getId());
            dto.setOrganizationName(logAction.getOrganization().getName());
        }
        dto.setRole(logAction.getRole());
        dto.setAction(logAction.getAction());
        dto.setItemSnapshot(logAction.getItemSnapshot());
        dto.setCreatedAt(logAction.getCreatedAt());
        return dto;
    }
}
