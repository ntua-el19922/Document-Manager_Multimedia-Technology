package com.medialab;

import com.medialab.model.Document;
import com.medialab.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    public void onNewDocument() {
        openEditor(null); // null σημαίνει ΝΕΟ έγγραφο
    }

    @FXML
    public void onOpenDocument() {
        Document selected = docsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Παρακαλώ επιλέξτε ένα έγγραφο!");
            alert.show();
            return;
        }
        openEditor(selected); // Περνάμε το επιλεγμένο για επεξεργασία
    }

    private void openEditor(Document doc) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/editor_view.fxml"));
            javafx.scene.Parent root = loader.load();

            // Περνάμε τα δεδομένα στον EditorController
            EditorController editorParams = loader.getController();

            // Βρίσκουμε τον τρέχοντα χρήστη (προσωρινά παίρνουμε τον πρώτο από τη λίστα ή τον admin)
            // Σημείωση: Αν η λίστα είναι άδεια (απίθανο αφού είσαι μέσα), αυτό ίσως θέλει προσοχή.
            User activeUser = DataManager.getUsers().isEmpty() ? null : DataManager.getUsers().get(0);

            editorParams.setContext(activeUser, doc);

            // Ανοίγουμε ΝΕΟ παράθυρο (Stage)
            Stage stage = new Stage();
            stage.setTitle(doc == null ? "Νέο Έγγραφο" : "Επεξεργασία: " + doc.getTitle());
            stage.setScene(new javafx.scene.Scene(root));

            // Όταν κλείσει το παράθυρο, ανανέωσε τον πίνακα!
            stage.setOnHidden(e -> refreshTable());

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        ObservableList<Document> data = FXCollections.observableArrayList(DataManager.getDocuments());
        docsTable.setItems(data);
    }
}