package com.bitvault.ui.components.alert;

import com.bitvault.ui.utils.IconUtils;
import com.bitvault.util.Messages;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorAlert {

    private static final Logger logger = LoggerFactory.getLogger(ErrorAlert.class);

    public static void show(String title, Exception error) {
        logger.error("", error);
        String message = Messages.i18n(error.getMessage());
        show(title, message);
    }

    public static void show(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtils.STAGE_ICON);
        alert.showAndWait();
    }


}
