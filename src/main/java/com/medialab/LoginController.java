package com.medialab;

import com.medialab.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
// Εισαγωγή κλάσεων για αλλαγή σκηνής (θα χρειαστούν αργότερα)
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        // Έλεγχος στοιχείων μέσω DataManager
        User foundUser = null;
        for (User user : DataManager.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser != null) {
            errorLabel.setText("Επιτυχία! Συνδέθηκε ο: " + foundUser.getUsername());
            // Εδώ αργότερα θα βάλουμε τον κώδικα που ανοίγει την κεντρική οθόνη
        } else {
            errorLabel.setText("Λάθος όνομα χρήστη ή κωδικός.");
        }
    }
}