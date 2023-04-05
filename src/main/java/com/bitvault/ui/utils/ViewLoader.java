package com.bitvault.ui.utils;

import com.bitvault.ui.exceptions.ViewLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Supplier;

public class ViewLoader {

    public static void load(Stage stage, Supplier<Parent> parentSupplier) {
        try {
            Parent view = parentSupplier.get();
            final Scene scene = new Scene(view);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (ViewLoadException ex) {
            ex.printStackTrace();
            //TODO show error window
        }
    }

    public static void loadFxml(Stage stage, String fxml) {
        String path = "/com.bitvault/scenes/%s".formatted(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(ViewLoader.class.getResource(path));

        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
