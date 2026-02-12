package com.medialab.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String type; // "admin", "author", "user"

    // Κενός constructor (ΑΠΑΡΑΙΤΗΤΟΣ για το JSON/Jackson)
    public User() {}

    public User(String username, String password, String fullName, String type) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.type = type;
    }

    // Getters και Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return fullName + " (" + type + ")";
    }
}