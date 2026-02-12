package com.medialab; // Προσοχή στο package!

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        // Φόρτωση δεδομένων ΠΡΙΝ ξεκινήσει το γραφικό περιβάλλον
        System.out.println("🔄 Αρχικοποίηση εφαρμογής...");
        DataManager.loadAllData();
    }

    @Override
    public void start(Stage stage) {
        // Ένα απλό παράθυρο για να δούμε ότι δουλεύει
        Label label = new Label("Καλώς ήρθατε!\nΧρήστες στη μνήμη: " + DataManager.getUsers().size());
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("MediaLab Documents");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Αποθήκευση κατά τον τερματισμό
        System.out.println("🛑 Τερματισμός εφαρμογής...");
        DataManager.saveAllData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}