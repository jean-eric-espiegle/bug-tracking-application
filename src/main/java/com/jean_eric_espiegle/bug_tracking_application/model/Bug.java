package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;

@Entity
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Profile reporter;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Support assignee;

    @Enumerated(EnumType.STRING)
    private BugStatus status = BugStatus.OPEN;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Profile getReporter() {
        return reporter;
    }

    public void setReporter(Profile reporter) {
        this.reporter = reporter;
    }

    public Support getAssignee() {
        return assignee;
    }

    public void setAssignee(Support assignee) {
        this.assignee = assignee;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }
}
