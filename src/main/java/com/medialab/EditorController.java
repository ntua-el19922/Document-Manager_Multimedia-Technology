package com.medialab;

import com.medialab.model.Category;
import com.medialab.model.Document;
import com.medialab.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditorController {

    @FXML private TextField titleField;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private TextArea contentArea;
    @FXML private Label statusLabel;
    @FXML private Button historyBtn;

    private User currentUser;
    private Document currentDocument; // αν είναι null -> νέο έγγραφο, αλλιώς -> επεξεργασία

    @FXML
    public void initialize() {
        // γέμισμα του dropdown με τις κατηγορίες που φτιάξαμε
        categoryCombo.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
    }

    // αυτή τη μέθοδο την καλούμε όταν ανοίγουμε το παράθυρο
    public void setContext(User user, Document doc) {
        this.currentUser = user;
        this.currentDocument = doc;

        // έλεγχος για το ιστορικό
        if ("user".equals(user.getType())) {
            if (historyBtn != null) historyBtn.setVisible(false); // Κρύψιμο κουμπιού
            titleField.setEditable(false);
            categoryCombo.setDisable(true);
            contentArea.setEditable(false); // Read-only mode
        }

        if (doc != null) {
            // λειτουργία επεξεργασίας: φόρτωσε τα στοιχεία
            titleField.setText(doc.getTitle());
            titleField.setEditable(false); // ο τίτλος δεν αλλάζει
            contentArea.setText(doc.getContent());

            // βρες και επέλεξε την σωστή κατηγορία στο ComboBox
            for (Category c : categoryCombo.getItems()) {
                if (c.getName().equals(doc.getCategoryName())) {
                    categoryCombo.setValue(c);
                    break;
                }
            }
            statusLabel.setText("Επεξεργασία: " + doc.getTitle() + " (v" + doc.getVersion() + ")");
        } else {
            // λειτουργία δημιουργίας
            statusLabel.setText("Νέο Έγγραφο");
        }
    }

    @FXML
    public void onSave() {
        String title = titleField.getText();
        Category cat = categoryCombo.getValue();
        String content = contentArea.getText();

        if (title.isEmpty() || cat == null || content.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Συμπληρώστε όλα τα πεδία!");
            alert.show();
            return;
        }

        if (currentDocument == null) {
            // δημιουργία νέου
            Document newDoc = new Document(title, currentUser.getUsername(), cat.getName(), content);
            DataManager.getDocuments().add(newDoc);
        } else {
            // ενημέρωση υπάρχοντος
            // ενημερώνουμε κατηγορία και περιεχόμενο (αυτό φτιάχνει και νέα έκδοση αυτόματα)
            currentDocument.setCategoryName(cat.getName());

            // αν άλλαξε το κείμενο, κάνε update
            if (!currentDocument.getContent().equals(content)) {
                currentDocument.updateContent(content);
            }
        }

        DataManager.saveAllData(); // αποθήκευση στο δίσκο

        // κλείσιμο παραθύρου
        ((Stage) titleField.getScene().getWindow()).close();
    }

    @FXML
    public void onHistory() {
        if (currentDocument == null || currentDocument.getPreviousVersions().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Δεν υπάρχει ιστορικό για αυτό το έγγραφο.");
            alert.show();
            return;
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/history_view.fxml"));
            javafx.scene.Parent root = loader.load();

            HistoryController controller = loader.getController();
            controller.setDocument(currentDocument);

            Stage stage = new Stage();
            stage.setTitle("Ιστορικό Αλλαγών");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel() {
        ((Stage) titleField.getScene().getWindow()).close();
    }
}