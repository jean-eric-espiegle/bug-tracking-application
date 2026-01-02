package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.model.Bug;
import com.jean_eric_espiegle.bug_tracking_application.repository.BugRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugRepository bugRepository;

    public BugController(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    @GetMapping
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        return bugRepository.save(bug);
    }

    @GetMapping("/{id}")
    public Bug getBug(@PathVariable Long id) {
        return bugRepository.findById(id).orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    @PutMapping("/{id}")
    public Bug updateBug(@PathVariable Long id, @RequestBody Bug bugDetails) {
        Bug bug = bugRepository.findById(id).orElseThrow(() -> new RuntimeException("Bug not found"));
        bug.setTitle(bugDetails.getTitle());
        bug.setDescription(bugDetails.getDescription());
        bug.setReporter(bugDetails.getReporter());
        bug.setAssignee(bugDetails.getAssignee());
        bug.setStatus(bugDetails.getStatus());
        return bugRepository.save(bug);
    }

    @DeleteMapping("/{id}")
    public void deleteBug(@PathVariable Long id) {
        bugRepository.deleteById(id);
    }
}
