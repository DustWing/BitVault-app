package com.bitvault.ui.utils;

import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.exceptions.ViewLoadException;
import com.bitvault.util.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

public class ViewLoader {

    public static Stage popUp(final Window owner, final Parent root, final String title) {

        final Scene scene = new Scene(root);

        final Stage stage = new Stage();

        final URL iconUrl = ResourceLoader.loadURL("/com.bitvault/icons/32moth.png");
        stage.getIcons().add(new Image(iconUrl.toExternalForm()));

        stage.setTitle(title);
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMinWidth(600);
        stage.setMinHeight(600);

        stage.setWidth(600);
        stage.setHeight(600);
        return stage;
    }


    public static void load(Stage stage, double width, double height, Supplier<Parent> parentSupplier) {
        try {
            stage.setWidth(width);
            stage.setHeight(height);
            Parent view = parentSupplier.get();
            final Scene scene = new Scene(view);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (ViewLoadException ex) {
            ex.printStackTrace();
            ErrorAlert.show("ViewLoader", ex);
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
