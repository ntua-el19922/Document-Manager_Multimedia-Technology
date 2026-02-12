package com.medialab.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;

    public Category() {} // Κενός constructor για JSON

    public Category(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name; // Αυτό βοηθάει να φαίνεται σωστά στα Dropdown αργότερα
    }
}