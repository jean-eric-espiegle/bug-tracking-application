package com.jean_eric_espiegle.bug_tracking_application.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jean_eric_espiegle.bug_tracking_application.model.LogAction;
import com.jean_eric_espiegle.bug_tracking_application.model.Membership;
import com.jean_eric_espiegle.bug_tracking_application.model.User;
import com.jean_eric_espiegle.bug_tracking_application.repository.LogActionRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.MembershipRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogAspect {

    private final LogActionRepository logActionRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public AuditLogAspect(
            LogActionRepository logActionRepository,
            MembershipRepository membershipRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.logActionRepository = logActionRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "@annotation(loggableAction)", returning = "result")
    public void audit(JoinPoint joinPoint, LoggableAction loggableAction, Object result) {
        try {
            User user = getAuthenticatedUser();
            if (user == null)
                return;

            Long organizationId = resolveOrganizationId(joinPoint.getArgs());
            if (organizationId == null)
                return;

            Membership membership = membershipRepository
                    .findByUserIdAndOrganizationId(user.getId(), organizationId)
                    .orElse(null);

            if (membership == null)
                return;

            LogAction log = new LogAction();
            log.setUser(user);
            log.setOrganization(membership.getOrganization());
            log.setRole(membership.getRole());
            log.setAction(loggableAction.action());
            log.setItemSnapshot(serialize(result));

            logActionRepository.save(log);

        } catch (Exception ignored) {
        }
    }

    private User getAuthenticatedUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return null;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    private Long resolveOrganizationId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long)
                return (Long) arg;
        }
        return null;
    }

    private String serialize(Object obj) {
        try {
            return obj == null ? "{}" : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{ \"error\": \"serialization_failed\" }";
        }
    }
}
