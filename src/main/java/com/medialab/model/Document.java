package com.medialab.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {
    private String title;
    private String authorName;   // Κρατάμε το username για ευκολία
    private String categoryName; // Κρατάμε το όνομα κατηγορίας
    private String content;      // Το τρέχον κείμενο
    private String creationDate; // Ημερομηνία ως κείμενο
    private int version;         // Αριθμός έκδοσης (π.χ. 1, 2, 3)

    // Λίστα για να κρατάμε τα παλιά κείμενα (Versioning)
    private List<String> previousVersions = new ArrayList<>();

    public Document() {} // Για το JSON

    public Document(String title, String authorName, String categoryName, String content) {
        this.title = title;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.content = content;
        this.version = 1; // Ξεκινάει από το 1 [cite: 24]

        // Αυτόματη ημερομηνία τώρα
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationDate = dtf.format(LocalDateTime.now());
    }

    // Μέθοδος για ενημέρωση κειμένου (δημιουργεί νέα έκδοση)
    public void updateContent(String newContent) {
        // 1. Κρατάμε το παλιό κείμενο στο ιστορικό
        previousVersions.add(this.content);

        // 2. Ενημερώνουμε με το νέο
        this.content = newContent;

        // 3. Αυξάνουμε την έκδοση
        this.version++;

        // 4. ΕΝΗΜΕΡΩΣΗ ΗΜΕΡΟΜΗΝΙΑΣ
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