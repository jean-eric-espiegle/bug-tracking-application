package com.jean_eric_espiegle.bug_tracking_application.repository;

import com.jean_eric_espiegle.bug_tracking_application.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
