package com.medialab.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String type; // "admin", "author", "user"

    // λίστα με ονόματα κατηγοριών που έχει πρόσβαση
    private List<String> allowedCategories = new ArrayList<>();

    // map για παρακολούθηση (τίτλος εγγράφου -> τελευταία έκδοση που είδε)
    private Map<String, Integer> followedDocs = new HashMap<>();

    public User() {}

    public User(String username, String password, String fullName, String type) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.type = type;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getAllowedCategories() { return allowedCategories; }
    public void setAllowedCategories(List<String> allowedCategories) { this.allowedCategories = allowedCategories; }

    public Map<String, Integer> getFollowedDocs() { return followedDocs; }
    public void setFollowedDocs(Map<String, Integer> followedDocs) { this.followedDocs = followedDocs; }

    @Override
    public String toString() { return fullName + " (" + type + ")"; }
}