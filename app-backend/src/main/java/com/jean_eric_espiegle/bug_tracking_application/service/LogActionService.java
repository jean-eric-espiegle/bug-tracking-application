package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.model.LogAction;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import com.jean_eric_espiegle.bug_tracking_application.repository.LogActionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogActionService {

    private final LogActionRepository logActionRepository;

    public LogActionService(LogActionRepository logActionRepository) {
        this.logActionRepository = logActionRepository;
    }

    public List<LogAction> getLogs(
            Long userId,
            Long organizationId,
            String roleStr,
            String action,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        Specification<LogAction> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }

        if (organizationId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("organization").get("id"), organizationId));
        }

        if (roleStr != null) {
            // Correct Enum conversion
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

        return logActionRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Page<LogAction> getLogsPage(
            Long userId,
            Long organizationId,
            String roleStr,
            String action,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size) {
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

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return logActionRepository.findAll(spec, pageable);
    }
}
