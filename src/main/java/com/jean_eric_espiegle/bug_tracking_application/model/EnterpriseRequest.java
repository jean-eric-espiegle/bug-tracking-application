package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "enterprise_requests")
public class EnterpriseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    private String companyName;
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
