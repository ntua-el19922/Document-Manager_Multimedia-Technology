package com.medialab;

import com.medialab.model.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DocumentsController {

    @FXML private TableView<Document> docsTable;
    @FXML private TableColumn<Document, String> titleCol;
    @FXML private TableColumn<Document, String> authorCol;
    @FXML private TableColumn<Document, String> categoryCol;
    @FXML private TableColumn<Document, String> dateCol;

    @FXML
    public void initialize() {
        // Σύνδεση στηλών με τα πεδία της κλάσης Document
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        refreshTable();
    }

    @FXML
    public void onOpenDocument() {
        Document selected = docsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Παρακαλώ επιλέξτε ένα έγγραφο!");
            alert.show();
            return;
        }

        System.out.println("Άνοιγμα εγγράφου: " + selected.getTitle());
        // ΕΔΩ θα βάλουμε αργότερα τον κώδικα που ανοίγει τον Editor
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Προσεχώς: Editor για το " + selected.getTitle());
        alert.show();
    }

    @FXML
    public void onNewDocument() {
        System.out.println("Δημιουργία νέου εγγράφου...");
        // ΕΔΩ θα βάλουμε αργότερα τον κώδικα για νέο έγγραφο
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Προσεχώς: Φόρμα δημιουργίας");
        alert.show();
    }

    private void refreshTable() {
        ObservableList<Document> data = FXCollections.observableArrayList(DataManager.getDocuments());
        docsTable.setItems(data);
    }
}