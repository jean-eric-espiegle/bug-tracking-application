package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @Enumerated(EnumType.STRING)
    private PlanType type;

    private int maxAdmins;
    private int maxSupport;
    private int maxUsers;

    // --- getters & setters ---

    public PlanType getType() {
        return type;
    }

    public void setType(PlanType type) {
        this.type = type;
    }

    public int getMaxAdmins() {
        return maxAdmins;
    }

    public void setMaxAdmins(int maxAdmins) {
        this.maxAdmins = maxAdmins;
    }

    public int getMaxSupport() {
        return maxSupport;
    }

    public void setMaxSupport(int maxSupport) {
        this.maxSupport = maxSupport;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }
}
