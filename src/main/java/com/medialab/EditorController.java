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
    private Document currentDocument; // Αν είναι null -> Νέο έγγραφο, αλλιώς -> Επεξεργασία

    @FXML
    public void initialize() {
        // Γέμισμα του Dropdown με τις κατηγορίες που φτιάξαμε
        categoryCombo.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
    }

    // Αυτή τη μέθοδο την καλούμε όταν ανοίγουμε το παράθυρο
    public void setContext(User user, Document doc) {
        this.currentUser = user;
        this.currentDocument = doc;

        // Έλεγχος για το Ιστορικό
        if ("user".equals(user.getType())) {
            if (historyBtn != null) historyBtn.setVisible(false); // Κρύψιμο κουμπιού
            titleField.setEditable(false);
            categoryCombo.setDisable(true);
            contentArea.setEditable(false); // Read-only mode
        }

        if (doc != null) {
            // Λειτουργία Επεξεργασίας: Φόρτωσε τα στοιχεία
            titleField.setText(doc.getTitle());
            titleField.setEditable(false); // Ο τίτλος δεν αλλάζει (ζητείται συχνά έτσι)
            contentArea.setText(doc.getContent());

            // Βρες και επέλεξε την σωστή κατηγορία στο ComboBox
            for (Category c : categoryCombo.getItems()) {
                if (c.getName().equals(doc.getCategoryName())) {
                    categoryCombo.setValue(c);
                    break;
                }
            }
            statusLabel.setText("Επεξεργασία: " + doc.getTitle() + " (v" + doc.getVersion() + ")");
        } else {
            // Λειτουργία Δημιουργίας
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
            // --- ΔΗΜΙΟΥΡΓΙΑ ΝΕΟΥ ---
            Document newDoc = new Document(title, currentUser.getUsername(), cat.getName(), content);
            DataManager.getDocuments().add(newDoc);
        } else {
            // --- ΕΝΗΜΕΡΩΣΗ ΥΠΑΡΧΟΝΤΟΣ ---
            // Ενημερώνουμε κατηγορία και περιεχόμενο (αυτό φτιάχνει και νέα έκδοση αυτόματα)
            currentDocument.setCategoryName(cat.getName());

            // Αν άλλαξε το κείμενο, κάνε update
            if (!currentDocument.getContent().equals(content)) {
                currentDocument.updateContent(content);
            }
        }

        DataManager.saveAllData(); // Αποθήκευση στο δίσκο

        // Κλείσιμο παραθύρου
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