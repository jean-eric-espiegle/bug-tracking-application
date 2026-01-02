package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_users")
public class Admin extends User {

    private boolean superAdmin = false;

    public Admin() {
    }

    public Admin(String username, String email, String password, boolean superAdmin) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        this.superAdmin = superAdmin;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }
}
