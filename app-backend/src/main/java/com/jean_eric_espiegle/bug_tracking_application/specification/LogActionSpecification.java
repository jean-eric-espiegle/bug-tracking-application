package com.jean_eric_espiegle.bug_tracking_application.specification;

import com.jean_eric_espiegle.bug_tracking_application.model.LogAction;
import com.jean_eric_espiegle.bug_tracking_application.model.Role;
import org.springframework.data.jpa.domain.Specification;

public class LogActionSpecification {

    public static Specification<LogAction> hasUser(Long userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<LogAction> hasOrganization(Long orgId) {
        return (root, query, cb) -> orgId == null ? null : cb.equal(root.get("organization").get("id"), orgId);
    }

    public static Specification<LogAction> hasRole(Role role) {
        return (root, query, cb) -> role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<LogAction> hasAction(String action) {
        return (root, query, cb) -> action == null ? null
                : cb.like(cb.lower(root.get("action")), "%" + action.toLowerCase() + "%");
    }
}
