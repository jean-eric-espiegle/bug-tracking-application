package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_type")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "enterprise_pending")
    private boolean enterprisePending = false;

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public boolean isEnterprisePending() {
        return enterprisePending;
    }

    public void setEnterprisePending(boolean enterprisePending) {
        this.enterprisePending = enterprisePending;
    }
}
