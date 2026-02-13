package com.medialab;

import com.medialab.model.Document;
import com.medialab.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DocumentsController {

    @FXML private TableView<Document> docsTable;
    @FXML private TableColumn<Document, String> titleCol;
    @FXML private TableColumn<Document, String> authorCol;
    @FXML private TableColumn<Document, String> categoryCol;
    @FXML private TableColumn<Document, String> dateCol;

    // ΝΕΑ ΚΟΥΜΠΙΑ ΚΑΙ ΠΕΔΙΑ ΑΝΑΖΗΤΗΣΗΣ
    @FXML private Button newDocBtn;
    @FXML private Button openDocBtn;
    @FXML private Button watchBtn; // Κουμπί παρακολούθησης
    @FXML private TextField searchField; // Πεδίο αναζήτησης
    @FXML private Button deleteDocBtn;

    private User currentUser;
    private ObservableList<Document> masterData = FXCollections.observableArrayList();

    public void setLoggedInUser(User user) {
        this.currentUser = user;
        setupPermissions();
        loadDocuments();
    }

    private void setupPermissions() {
        // Αν είναι απλός user, κρύψε το κουμπί "Νέο Έγγραφο" [cite: 19, 20]
        if ("user".equals(currentUser.getType())) {
            newDocBtn.setVisible(false);
            deleteDocBtn.setVisible(false); // Κρύψιμο διαγραφής για απλό χρήστη
            openDocBtn.setText("Προβολή"); // Αλλαγή κειμένου
        } else {
            newDocBtn.setVisible(true);
            deleteDocBtn.setVisible(true); // Εμφάνιση για Admin & Author
            openDocBtn.setText("Άνοιγμα / Επεξεργασία");
        }
    }

    @FXML
    public void initialize() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        // Listener για την αναζήτηση [cite: 17, 79]
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterList(newValue));
        }
    }

    private void loadDocuments() {
        masterData.clear();

        // Φιλτράρισμα βάσει δικαιωμάτων κατηγορίας [cite: 12]
        for (Document doc : DataManager.getDocuments()) {
            // Ο admin και ο author βλέπουν τα πάντα ή πρέπει να ελέγχεται;
            // Η εκφώνηση λέει ο Author βλέπει ό,τι του έχει ανατεθεί. Ο Admin όλα.
            if ("admin".equals(currentUser.getType())) {
                masterData.add(doc);
            } else {
                // ΕΛΕΓΧΟΣ: Αγνοούμε κεφαλαία/πεζά (Case Insensitive)
                boolean hasAccess = false;
                for (String allowedCat : currentUser.getAllowedCategories()) {
                    if (allowedCat.trim().equalsIgnoreCase(doc.getCategoryName().trim())) {
                        hasAccess = true;
                        break;
                    }
                }

                if (hasAccess) {
                    masterData.add(doc);
                }
            }
        }
        docsTable.setItems(masterData);
    }

    private void filterList(String text) {
        if (text == null || text.isEmpty()) {
            docsTable.setItems(masterData);
            return;
        }
        // Αναζήτηση σε Τίτλο, Συγγραφέα ή Κατηγορία [cite: 17]
        FilteredList<Document> filtered = new FilteredList<>(masterData, doc ->
                doc.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                        doc.getAuthorName().toLowerCase().contains(text.toLowerCase()) ||
                        doc.getCategoryName().toLowerCase().contains(text.toLowerCase())
        );
        docsTable.setItems(filtered);
    }

    @FXML
    public void onNewDocument() {
        openEditor(null);
    }

    @FXML
    public void onOpenDocument() {
        Document selected = docsTable.getSelectionModel().getSelectedItem();
        if (selected != null) openEditor(selected);
    }

    @FXML
    public void onWatchToggle() {
        Document selected = docsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Λογική Παρακολούθησης [cite: 15, 17, 81]
        if (currentUser.getFollowedDocs().containsKey(selected.getTitle())) {
            currentUser.getFollowedDocs().remove(selected.getTitle());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Σταματήσατε να παρακολουθείτε το έγγραφο.");
            alert.show();
        } else {
            currentUser.getFollowedDocs().put(selected.getTitle(), selected.getVersion());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Παρακολουθείτε το έγγραφο για αλλαγές.");
            alert.show();
        }
        DataManager.saveAllData(); // Σώσε τις προτιμήσεις του χρήστη
    }

    @FXML
    public void onDeleteDocument() {
        Document selected = docsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Παρακαλώ επιλέξτε ένα έγγραφο προς διαγραφή.");
            alert.show();
            return;
        }

        // Επιβεβαίωση διαγραφής
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Είστε σίγουρος ότι θέλετε να διαγράψετε το έγγραφο '" + selected.getTitle() + "';", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Επιβεβαίωση Διαγραφής");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            // 1. Διαγραφή από τη λίστα στη μνήμη (DataManager)
            DataManager.getDocuments().remove(selected);

            // 2. Μόνιμη αποθήκευση στο JSON
            DataManager.saveAllData();

            // 3. Ανανέωση του πίνακα στην οθόνη
            loadDocuments();

            // Ενημέρωση χρήστη
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Το έγγραφο διαγράφηκε επιτυχώς.");
            success.show();
        }
    }

    private void openEditor(Document doc) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/editor_view.fxml"));
            javafx.scene.Parent root = loader.load();

            EditorController editorParams = loader.getController();

            // ΔΙΟΡΘΩΣΗ BUG 1 & 2: Περνάμε τον ΣΩΣΤΟ χρήστη (currentUser) όχι τον admin
            editorParams.setContext(currentUser, doc);

            Stage stage = new Stage();
            stage.setTitle(doc == null ? "Νέο Έγγραφο" : "Επεξεργασία: " + doc.getTitle());
            stage.setScene(new javafx.scene.Scene(root));
            stage.setOnHidden(e -> loadDocuments()); // Reload όταν κλείσει
            stage.show();

        } catch (Exception e) { e.printStackTrace(); }
    }
}