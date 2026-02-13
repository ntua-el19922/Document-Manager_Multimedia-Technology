package com.medialab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.medialab.model.User;
import com.medialab.model.Category;
import com.medialab.model.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση DataManager είναι υπεύθυνη για τη διαχείριση της ροής δεδομένων της εφαρμογής.
 * <p>
 * Λειτουργεί ως το κεντρικό σημείο αποθήκευσης (in-memory storage) και αναλαμβάνει
 * τη φόρτωση και αποθήκευση των δεδομένων (Χρήστες, Κατηγορίες, Έγγραφα)
 * σε αρχεία μορφής JSON.
 * </p>
 *
 * @author viktorouli
 * @version 1.0
 */
public class DataManager {

    private static final String FOLDER_PATH = "medialab";
    private static final String USERS_FILE = "medialab/users.json";
    private static final String CATEGORIES_FILE = "medialab/categories.json";
    private static final String DOCUMENTS_FILE = "medialab/documents.json";

    // στατικές λίστες που κρατούν τα δεδομένα στη μνήμη κατά την εκτέλεση
    private static List<User> users = new ArrayList<>();
    private static List<Category> categories = new ArrayList<>();
    private static List<Document> documents = new ArrayList<>();

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Φορτώνει όλα τα δεδομένα (Χρήστες, Κατηγορίες, Έγγραφα) από τα αρχεία JSON.
     * <p>
     * Ελέγχει αν υπάρχει ο φάκελος 'medialab' και τα αντίστοιχα αρχεία.
     * Αν δεν βρεθεί αρχείο χρηστών, δημιουργεί αυτόματα τον Default Admin.
     * </p>
     */
    public static void loadAllData() {
        try {
            File dir = new File(FOLDER_PATH);
            if (!dir.exists()) {
                boolean created = dir.mkdir();
                if (created) System.out.println("Δημιουργήθηκε ο φάκελος αποθήκευσης.");
            }

            // φόρτωση χρηστών
            File userFile = new File(USERS_FILE);
            if (userFile.exists() && userFile.length() > 0) {
                users = mapper.readValue(userFile, new TypeReference<List<User>>() {});
            } else {
                users = new ArrayList<>();
                users.add(new User("medialab", "medialab_2025", "Default Admin", "admin"));
                saveAllData();
            }

            // χόρτωση κατηγοριών
            File catFile = new File(CATEGORIES_FILE);
            if (catFile.exists() && catFile.length() > 0) {
                categories = mapper.readValue(catFile, new TypeReference<List<Category>>() {});
            }

            // φόρτωση Εγγράφων
            File docFile = new File(DOCUMENTS_FILE);
            if (docFile.exists() && docFile.length() > 0) {
                documents = mapper.readValue(docFile, new TypeReference<List<Document>>() {});
            }

        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση δεδομένων: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Αποθηκεύει την τρέχουσα κατάσταση των δεδομένων στα αντίστοιχα αρχεία JSON.
     * <p>
     * Χρησιμοποιεί τη βιβλιοθήκη Jackson για τη σειριοποίηση των αντικειμένων.
     * Καλείται συνήθως μετά από κάθε αλλαγή (προσθήκη, επεξεργασία, διαγραφή)
     * ή κατά τον τερματισμό της εφαρμογής.
     * </p>
     */
    public static void saveAllData() {
        try {
            if (users != null)
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USERS_FILE), users);

            if (categories != null)
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CATEGORIES_FILE), categories);

            if (documents != null)
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DOCUMENTS_FILE), documents);

        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Επιστρέφει τη λίστα με τους χρήστες της εφαρμογής.
     *
     * @return Μια λίστα (List) με αντικείμενα τύπου {@link User}.
     */
    public static List<User> getUsers() {
        return users;
    }

    /**
     * Επιστρέφει τη λίστα με τις διαθέσιμες κατηγορίες εγγράφων.
     *
     * @return Μια λίστα (List) με αντικείμενα τύπου {@link Category}.
     */
    public static List<Category> getCategories() {
        return categories;
    }

    /**
     * Επιστρέφει τη λίστα με τα αποθηκευμένα έγγραφα.
     *
     * @return Μια λίστα (List) με αντικείμενα τύπου {@link Document}.
     */
    public static List<Document> getDocuments() {
        return documents;
    }
}