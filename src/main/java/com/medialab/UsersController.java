package com.medialab;

import com.medialab.model.Category;
import com.medialab.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;

public class UsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> fullnameCol;
    @FXML private TableColumn<User, String> typeCol;
    @FXML private TableColumn<User, String> catsCol;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullnameField;
    @FXML private ComboBox<String> typeCombo;

    // ΝΕΟ: Η λίστα με τις κατηγορίες αντί για TextField
    @FXML private ListView<String> catsListView;

    @FXML
    public void initialize() {
        // Ρύθμιση Στηλών
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        catsCol.setCellValueFactory(cellData -> {
            List<String> list = cellData.getValue().getAllowedCategories();
            String asString = (list == null || list.isEmpty()) ? "-" : String.join(", ", list);
            return new javafx.beans.property.SimpleStringProperty(asString);
        });

        // Ρύθμιση ComboBox
        typeCombo.setItems(FXCollections.observableArrayList("admin", "author", "user"));
        typeCombo.setValue("user");

        // Ρύθμιση Λίστας Κατηγοριών (Πολλαπλή επιλογή)
        catsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadCategoriesToList();

        // Listener: Όταν πατάς σε χρήστη στον πίνακα, γέμισε τη φόρμα
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });

        refreshTable();
    }

    private void loadCategoriesToList() {
        // Παίρνουμε τα ονόματα από όλες τις υπάρχουσες κατηγορίες
        List<String> catNames = new ArrayList<>();
        for (Category c : DataManager.getCategories()) {
            catNames.add(c.getName());
        }
        catsListView.setItems(FXCollections.observableArrayList(catNames));
    }

    private void fillForm(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        fullnameField.setText(user.getFullName());
        typeCombo.setValue(user.getType());

        // Επιλογή κατηγοριών στη λίστα
        catsListView.getSelectionModel().clearSelection();
        for (String allowed : user.getAllowedCategories()) {
            catsListView.getSelectionModel().select(allowed);
        }
    }

    @FXML
    public void onAddUser() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String name = fullnameField.getText();
        String type = typeCombo.getValue();

        if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            showAlert("Σφάλμα", "Συμπληρώστε όλα τα πεδία!");
            return;
        }

        // Έλεγχος αν υπάρχει ήδη
        for (User u : DataManager.getUsers()) {
            if (u.getUsername().equals(user)) {
                showAlert("Σφάλμα", "Το username υπάρχει ήδη!");
                return;
            }
        }

        User newUser = new User(user, pass, name, type);

        // Αποθήκευση επιλεγμένων κατηγοριών από τη λίστα
        List<String> selectedCats = catsListView.getSelectionModel().getSelectedItems();
        newUser.setAllowedCategories(new ArrayList<>(selectedCats));

        DataManager.getUsers().add(newUser);
        DataManager.saveAllData();
        refreshTable();
        onClearForm();
    }

    @FXML
    public void onUpdateUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Προσοχή", "Επιλέξτε έναν χρήστη από τον πίνακα για ενημέρωση.");
            return;
        }

        // Ενημέρωση στοιχείων
        selected.setUsername(usernameField.getText());
        selected.setPassword(passwordField.getText());
        selected.setFullName(fullnameField.getText());
        selected.setType(typeCombo.getValue());

        // Ενημέρωση κατηγοριών
        List<String> selectedCats = catsListView.getSelectionModel().getSelectedItems();
        selected.setAllowedCategories(new ArrayList<>(selectedCats));

        DataManager.saveAllData();
        refreshTable();
        showAlert("Επιτυχία", "Τα στοιχεία του χρήστη ενημερώθηκαν.");
    }

    @FXML
    public void onDeleteUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if ("medialab".equals(selected.getUsername())) {
            showAlert("Απαγορεύεται", "Δεν μπορείτε να διαγράψετε τον κεντρικό διαχειριστή!");
            return;
        }

        DataManager.getUsers().remove(selected);
        DataManager.saveAllData();
        refreshTable();
        onClearForm();
    }

    @FXML
    public void onClearForm() {
        usersTable.getSelectionModel().clearSelection();
        usernameField.clear();
        passwordField.clear();
        fullnameField.clear();
        typeCombo.setValue("user");
        catsListView.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        // Ξαναφορτώνουμε τις κατηγορίες μήπως προστέθηκαν καινούργιες
        loadCategoriesToList();
        ObservableList<User> data = FXCollections.observableArrayList(DataManager.getUsers());
        usersTable.setItems(data);
        usersTable.refresh();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}