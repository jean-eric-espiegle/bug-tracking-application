package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.model.Bug;
import com.jean_eric_espiegle.bug_tracking_application.model.Profile;
import com.jean_eric_espiegle.bug_tracking_application.model.Support;
import com.jean_eric_espiegle.bug_tracking_application.repository.BugRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.ProfileRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.SupportRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugRepository bugRepository;
    private final ProfileRepository profileRepository;
    private final SupportRepository supportRepository;

    public BugController(BugRepository bugRepository, ProfileRepository profileRepository,
            SupportRepository supportRepository) {
        this.bugRepository = bugRepository;
        this.profileRepository = profileRepository;
        this.supportRepository = supportRepository;
    }

    @GetMapping
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bug getBug(@PathVariable Long id) {
        return bugRepository.findById(id).orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    @PostMapping
    public Bug createBug(@RequestBody BugRequest bugRequest) {
        Bug bug = new Bug();
        bug.setTitle(bugRequest.getTitle());
        bug.setDescription(bugRequest.getDescription());
        bug.setStatus(bugRequest.getStatus());

        // Set reporter
        Profile reporter = profileRepository.findById(bugRequest.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        bug.setReporter(reporter);

        // Set assignee
        Support assignee = supportRepository.findById(bugRequest.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        bug.setAssignee(assignee);

        return bugRepository.save(bug);
    }

    @PutMapping("/{id}")
    public Bug updateBug(@PathVariable Long id, @RequestBody BugRequest bugRequest) {
        Bug bug = bugRepository.findById(id).orElseThrow(() -> new RuntimeException("Bug not found"));
        bug.setTitle(bugRequest.getTitle());
        bug.setDescription(bugRequest.getDescription());
        bug.setStatus(bugRequest.getStatus());

        Profile reporter = profileRepository.findById(bugRequest.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        bug.setReporter(reporter);

        Support assignee = supportRepository.findById(bugRequest.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        bug.setAssignee(assignee);

        return bugRepository.save(bug);
    }

    @DeleteMapping("/{id}")
    public void deleteBug(@PathVariable Long id) {
        bugRepository.deleteById(id);
    }

    // --- DTO for requests ---
    public static class BugRequest {
        private String title;
        private String description;
        private Long reporterId;
        private Long assigneeId;
        private com.jean_eric_espiegle.bug_tracking_application.model.BugStatus status;

        // getters & setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getReporterId() {
            return reporterId;
        }

        public void setReporterId(Long reporterId) {
            this.reporterId = reporterId;
        }

        public Long getAssigneeId() {
            return assigneeId;
        }

        public void setAssigneeId(Long assigneeId) {
            this.assigneeId = assigneeId;
        }

        public com.jean_eric_espiegle.bug_tracking_application.model.BugStatus getStatus() {
            return status;
        }

        public void setStatus(com.jean_eric_espiegle.bug_tracking_application.model.BugStatus status) {
            this.status = status;
        }
    }
}
