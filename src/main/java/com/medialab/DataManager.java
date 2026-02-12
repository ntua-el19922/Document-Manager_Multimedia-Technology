package com.medialab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.medialab.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    // Φάκελος και αρχείο όπως ορίζει η εκφώνηση [cite: 46]
    private static final String FOLDER_PATH = "medialab";
    private static final String USERS_FILE = "medialab/users.json";

    private static final String CATEGORIES_FILE = "medialab/categories.json";
    private static java.util.List<com.medialab.model.Category> categories = new java.util.ArrayList<>();

    // Η μνήμη της εφαρμογής (Στατική λίστα)
    private static List<User> users = new ArrayList<>();
    private static ObjectMapper mapper = new ObjectMapper();

    // 1. Φόρτωση (Load) - Καλείται ΜΟΝΟ στην αρχή
    public static void loadAllData() {
        try {
            // Δημιουργία φακέλου αν δεν υπάρχει
            File dir = new File(FOLDER_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(USERS_FILE);
            // Αν υπάρχει αρχείο και δεν είναι άδειο, διάβασε το
            if (file.exists() && file.length() > 0) {
                users = mapper.readValue(file, new TypeReference<List<User>>() {});
                System.out.println("✅ Φορτώθηκαν " + users.size() + " χρήστες.");
            } else {
                // Αν ΔΕΝ υπάρχει, δημιούργησε τον Default Admin
                System.out.println("⚠️ Δεν βρέθηκαν χρήστες. Δημιουργία Default Admin.");
                users = new ArrayList<>();
                users.add(new User("medialab", "medialab_2025", "Default Admin", "admin"));
                saveAllData(); // Τον σώζουμε αμέσως
            }

            // --- Φόρτωση Κατηγοριών ---
            File catFile = new File(CATEGORIES_FILE);
            if (catFile.exists() && catFile.length() > 0) {
                categories = mapper.readValue(catFile, new TypeReference<java.util.List<com.medialab.model.Category>>() {});
                System.out.println("✅ Φορτώθηκαν " + categories.size() + " κατηγορίες.");
            } else {
                categories = new java.util.ArrayList<>();
                System.out.println("⚠️ Δεν βρέθηκαν κατηγορίες.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. Αποθήκευση (Save) - Καλείται ΜΟΝΟ στο τέλος [cite: 53]
    public static void saveAllData() {
        try {
            // Ασφαλιστική δικλείδα: Μην γράφεις αν η λίστα είναι null!
            if (users != null && !users.isEmpty()) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USERS_FILE), users);
                System.out.println("💾 Αποθηκεύτηκαν " + users.size() + " χρήστες.");
            } else {
                System.err.println("❌ ΠΡΟΣΟΧΗ: Η λίστα χρηστών είναι κενή. Ακύρωση εγγραφής.");
            }

            // --- Αποθήκευση Κατηγοριών ---
            if (categories != null) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CATEGORIES_FILE), categories);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsers() {
        return users;
    }

    public static java.util.List<com.medialab.model.Category> getCategories() {
        return categories;
    }
}
