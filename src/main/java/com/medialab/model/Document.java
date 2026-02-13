package com.medialab.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {
    private String title;
    private String authorName;   //  username
    private String categoryName;
    private String content;
    private String creationDate; // ημερομηνία ως κείμενο
    private int version;         // αριθμός έκδοσης

    // λίστα για τα παλιά κείμενα (versioning)
    private List<String> previousVersions = new ArrayList<>();

    public Document() {} // για το JSON

    public Document(String title, String authorName, String categoryName, String content) {
        this.title = title;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.content = content;
        this.version = 1; // ξεκινάει από το 1

        // αυτόματη ημερομηνία
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationDate = dtf.format(LocalDateTime.now());
    }

    // μέθοδος για ενημέρωση κειμένου (δημιουργεί νέα έκδοση)
    public void updateContent(String newContent) {
        // κρατάμε το παλιό κείμενο στο ιστορικό
        previousVersions.add(this.content);

        // ενημερώνουμε με το νέο
        this.content = newContent;

        // αυξάνουμε την έκδοση
        this.version++;

        // ενημέρωση ημερομηνίας
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationDate = dtf.format(LocalDateTime.now());
    }

    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public List<String> getPreviousVersions() { return previousVersions; }
    public void setPreviousVersions(List<String> previousVersions) { this.previousVersions = previousVersions; }
}