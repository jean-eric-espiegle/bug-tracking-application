package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.EnterpriseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRequestRepository extends JpaRepository<EnterpriseRequest, Long> {
}
