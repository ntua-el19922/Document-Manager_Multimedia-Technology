package com.medialab;

import com.medialab.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu; // Import για το Menu
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Label userInfoLabel; // Άλλαξε όνομα στο FXML

    @FXML
    private Menu adminMenu; // Το μενού διαχείρισης

    private User currentUser;

    public void setLoggedInUser(User user) {
        this.currentUser = user;

        // Ενημέρωση πληροφοριών αριστερά
        userInfoLabel.setText("Χρήστης:\n" + user.getUsername() + "\n(" + user.getType() + ")");

        // Έλεγχος δικαιωμάτων: Αν ΔΕΝ είναι admin, κρύψε το μενού διαχείρισης [cite: 29]
        if (!"admin".equals(user.getType())) {
            adminMenu.setVisible(false);
        }
    }

    @FXML
    public void onLogoutClick(ActionEvent event) throws IOException {
        // Η λογική αποσύνδεσης παραμένει ίδια
        // (Προσοχή: εδώ το event έρχεται από MenuItem, ίσως χρειαστεί αλλαγή στον τρόπο εύρεσης του Stage)

        // Ασφαλής τρόπος για να βρούμε το παράθυρο από MenuItem
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        // Χρησιμοποιούμε το adminMenu (ή οποιοδήποτε Node της σκηνής) για να βρούμε το Stage
        Stage stage = (Stage) userInfoLabel.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("MediaLab Documents Login");
        stage.centerOnScreen();
    }
}