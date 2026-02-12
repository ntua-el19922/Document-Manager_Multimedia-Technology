package com.medialab;

import com.medialab.model.Document;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HistoryController {

    @FXML private ListView<String> historyList;
    @FXML private Label titleLabel;

    public void setDocument(Document doc) {
        titleLabel.setText("Ιστορικό: " + doc.getTitle());

        java.util.List<String> formattedHistory = new java.util.ArrayList<>();
        int ver = 1;
        // Προσθέτουμε τις παλιές εκδόσεις
        for (String oldContent : doc.getPreviousVersions()) {
            formattedHistory.add("Έκδοση " + ver + ":\n" + oldContent); // Το \n αλλάζει γραμμή
            ver++;
        }

        if (formattedHistory.isEmpty()) {
            formattedHistory.add("Δεν υπάρχουν παλαιότερες εκδόσεις.");
        }

        historyList.setItems(FXCollections.observableArrayList(formattedHistory));
    }

    @FXML
    public void onClose() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }
}