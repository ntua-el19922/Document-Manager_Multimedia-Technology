package com.medialab;

import com.medialab.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MainController {

    @FXML
    private Label welcomeLabel;

    private User currentUser;

    // Αυτή η μέθοδος θα καλείται από τον LoginController για να ξέρουμε ποιος μπήκε
    public void setLoggedInUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Καλώς ήρθες, " + user.getFullName() + " (" + user.getType() + ")");
    }

    @FXML
    public void onLogoutClick(ActionEvent event) throws IOException {
        // Επιστροφή στο Login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("MediaLab Documents Login");
    }
}