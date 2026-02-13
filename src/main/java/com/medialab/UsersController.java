package com.medialab;

import com.medialab.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> fullnameCol;
    @FXML private TableColumn<User, String> typeCol;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullnameField;
    @FXML private ComboBox<String> typeCombo; // Επιλογή τύπου χρήστη
    @FXML private TextField categoriesField;

    @FXML
    public void initialize() {
        // 1. Ρύθμιση των στηλών του πίνακα (να ταιριάζουν με τα πεδία της κλάσης User)
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        // 2. Γέμισμα του ComboBox με επιλογές
        typeCombo.setItems(FXCollections.observableArrayList("admin", "author", "user"));
        typeCombo.setValue("user"); // Προεπιλογή

        // 3. Φόρτωση δεδομένων στον πίνακα
        refreshTable();
    }

    @FXML
    public void onAddUser() {
        // Λήψη τιμών από τη φόρμα
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String name = fullnameField.getText();
        String type = typeCombo.getValue();
        String catsText = categoriesField.getText(); // Π.χ. "Coffee"

        if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            showAlert("Σφάλμα", "Συμπληρώστε όλα τα πεδία!");
            return;
        }

        // Έλεγχος αν υπάρχει ήδη
        for (User u : DataManager.getUsers()) {
            if (u.getUsername().equals(user)) {
                showAlert("Σφάλμα", "Το username υπάρχει ήδη!");
                return;
            }
        }

        // Δημιουργία και αποθήκευση
        User newUser = new User(user, pass, name, type);
        // Αποθήκευση επιτρεπτών κατηγοριών
        if (!catsText.isEmpty()) {
            String[] cats = catsText.split(",");
            for (String c : cats) {
                newUser.getAllowedCategories().add(c.trim());
            }
        }

        DataManager.getUsers().add(newUser); // Προσθήκη στη μνήμη
        DataManager.saveAllData(); // Αποθήκευση στο αρχείο

        refreshTable(); // Ανανέωση πίνακα
        clearForm();    // Καθαρισμός πεδίων
    }

    @FXML
    public void onDeleteUser() {
        // Βρες ποιον επέλεξε ο χρήστης στον πίνακα
        User selected = usersTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Προσοχή", "Παρακαλώ επιλέξτε έναν χρήστη προς διαγραφή.");
            return;
        }

        if (selected.getUsername().equals("medialab")) {
            showAlert("Απαγορεύεται", "Δεν μπορείτε να διαγράψετε τον κεντρικό διαχειριστή!");
            return;
        }

        // Διαγραφή
        DataManager.getUsers().remove(selected);
        DataManager.saveAllData();
        refreshTable();
    }

    private void refreshTable() {
        // Μετατροπή της λίστας μας σε μορφή που καταλαβαίνει το JavaFX
        ObservableList<User> data = FXCollections.observableArrayList(DataManager.getUsers());
        usersTable.setItems(data);
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        fullnameField.clear();
        typeCombo.setValue("user");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}