package com.medialab;

import com.medialab.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu; // Import για το Menu
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Label userInfoLabel; // Άλλαξε όνομα στο FXML

    @FXML
    private Menu adminMenu; // Το μενού διαχείρισης

    private User currentUser;

    @FXML private Label statsLabel;
    @FXML private Label totalDocsLabel;
    @FXML private Label totalCatsLabel;

    public void setLoggedInUser(User user) {
        this.currentUser = user;

        // Ενημέρωση πληροφοριών αριστερά
        userInfoLabel.setText("Χρήστης:\n" + user.getUsername() + "\n(" + user.getType() + ")");

        // Έλεγχος δικαιωμάτων: Αν ΔΕΝ είναι admin, κρύψε το μενού διαχείρισης [cite: 29]
        if (!"admin".equals(user.getType())) {
            adminMenu.setVisible(false);
        }

        totalDocsLabel.setText("Συνολικά Έγγραφα: " + DataManager.getDocuments().size());
        totalCatsLabel.setText("Κατηγορίες: " + DataManager.getCategories().size());
    }

    @FXML private StackPane contentArea;

    @FXML
    public void onUsersMenuClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/users_view.fxml"));
            Node view = loader.load();

            // Καθαρίζουμε το κέντρο και βάζουμε το νέο view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCategoriesMenuClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categories_view.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDocsMenuClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/documents_view.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
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