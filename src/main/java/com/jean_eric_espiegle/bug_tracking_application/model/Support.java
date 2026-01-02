package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "support_users")
public class Support extends User {

    private String department; // optional field for support team assignment

    public Support() {
    }

    public Support(String username, String email, String password, String department) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
