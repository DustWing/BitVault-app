package com.bitvault.ui.utils;

import com.bitvault.ui.exceptions.ViewLoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Supplier;

public class ViewLoader {

    public static void load(Stage stage, Supplier<Parent> parentSupplier) {

        try {

            Parent view = parentSupplier.get();
            final Scene scene = new Scene(view, 840, 600);
            stage.setScene(scene);
            stage.show();
        } catch (ViewLoadException ex) {
            ex.printStackTrace();
            //TODO show error window
        }

    }
}
