package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "enterprise_pending")
    private boolean enterprisePending = false;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnterprisePending() {
        return enterprisePending;
    }

    public void setEnterprisePending(boolean enterprisePending) {
        this.enterprisePending = enterprisePending;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }
}
