package com.jean_eric_espiegle.bug_tracking_application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class Profile extends User {

    private String firstName;
    private String lastName;

    public Profile() {
    }

    public Profile(String username, String email, String password, String firstName, String lastName) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
