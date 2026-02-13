package com.medialab;

import com.medialab.model.Category;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoriesController {

    @FXML private TableView<Category> table;
    @FXML private TableColumn<Category, String> nameCol;
    @FXML private TextField nameField;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        refreshTable();
    }

    @FXML
    public void onAdd() {
        String name = nameField.getText();
        if (name.isEmpty()) return;

        // έλεγχος διπλότυπων
        for (Category c : DataManager.getCategories()) {
            if (c.getName().equalsIgnoreCase(name)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Η κατηγορία υπάρχει ήδη!");
                alert.show();
                return;
            }
        }

        DataManager.getCategories().add(new Category(name));
        DataManager.saveAllData();
        refreshTable();
        nameField.clear();
    }

    @FXML
    public void onDelete() {
        Category selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DataManager.getCategories().remove(selected);
            DataManager.saveAllData();
            refreshTable();
        }
    }

    // η επεξεργασία (rename)
    @FXML
    public void onEdit() {
        Category selected = table.getSelectionModel().getSelectedItem();
        String newName = nameField.getText();
        if (selected != null && !newName.isEmpty()) {
            selected.setName(newName);
            DataManager.saveAllData();
            refreshTable();
            nameField.clear();
        }
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
    }
}