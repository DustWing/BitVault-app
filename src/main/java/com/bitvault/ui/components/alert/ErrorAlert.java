package com.bitvault.ui.components.alert;

import com.bitvault.util.Messages;
import javafx.scene.control.Alert;

public class ErrorAlert {

    public static void show(String title, String msg) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void show(String title, Exception error) {
        String message = Messages.i18n(error.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
