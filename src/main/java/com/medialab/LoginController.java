package com.medialab;

import com.medialab.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
// Εισαγωγή κλάσεων για αλλαγή σκηνής (θα χρειαστούν αργότερα)
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

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
            // Αν βρέθηκε χρήστης, αλλάζουμε σκηνή
            try {
                // 1. Φόρτωση του main_screen.fxml
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main_screen.fxml"));
                javafx.scene.Parent root = loader.load();

                // 2. Πέρασμα του χρήστη στον MainController
                MainController controller = loader.getController();
                controller.setLoggedInUser(foundUser); // <-- Εδώ στέλνουμε το "ποιος μπήκε"

                // 3. Εμφάνιση της νέας σκηνής στο ίδιο παράθυρο
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("MediaLab - Κεντρική Οθόνη");
                stage.centerOnScreen(); // Κεντράρισμα στην οθόνη

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Σφάλμα φόρτωσης της εφαρμογής.");
            }
        } else {
            errorLabel.setText("Λάθος όνομα χρήστη ή κωδικός.");
        }
    }
}