package com.oneenterprise.userservice.model;

/**
 * Internal representation of a user.
 * This is deliberately NOT exposed directly over the API — the controller
 * returns a UserDto instead, so internal fields can change without
 * breaking consumers like Order Service.
 */
public class User {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String status; // e.g. ACTIVE, SUSPENDED — internal detail

    public User(Long id, String fullName, String email, String status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
