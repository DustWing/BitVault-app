package com.bitvault.ui.components;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class BitVaultAlert {

    public static void show(String title, String msg) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
