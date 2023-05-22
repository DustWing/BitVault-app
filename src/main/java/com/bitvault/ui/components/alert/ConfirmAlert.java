package com.bitvault.ui.components.alert;

import com.bitvault.security.UserSession;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.components.PasswordInputDialog;
import com.bitvault.ui.model.User;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmAlert {

    public static boolean deleteConfirm(UserSession userSession, boolean requiresMp) {

        final String title = Labels.i18n("delete.title");
        final String header = Labels.i18n("delete.header");

        if (requiresMp && !userSession.isAuthOnCoolDown()) {
            boolean authenticate = authenticate(userSession, title, header);
            if (authenticate) {
                userSession.putOnAuthCoolDown();
            }

            return authenticate;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Labels.i18n("delete.context"));
        Optional<ButtonType> buttonType = alert.showAndWait();

        return buttonType.filter(ButtonType.OK::equals).isPresent();

    }

    public static boolean editAuthenticate(UserSession userSession, boolean requiresMp) {

        if (requiresMp && !userSession.isAuthOnCoolDown()) {
            String title = Labels.i18n("edit.record");
            String header = Labels.i18n("edit.header");
            boolean authenticate = authenticate(userSession, title, header);
            if (authenticate) {
                userSession.putOnAuthCoolDown();
            }

            return authenticate;
        }
        return true;
    }

    public static boolean authenticate(UserSession userSession, String titleText, String headerText) {

        String username = userSession.getUsername();
        IUserService userService = userSession.getServiceFactory().getUserService();

        PasswordInputDialog textInputDialog = new PasswordInputDialog();
        textInputDialog.setTitle(titleText);
        textInputDialog.setHeaderText(headerText);
        textInputDialog.setContentText(Labels.i18n("password"));

        Optional<String> input = textInputDialog.showAndWait();

        if (input.isEmpty()) {
            return false;
        }

        Result<User> authenticate = userService.authenticate(username, input.get());

        if (authenticate.hasError()) {
            ErrorAlert.show(Labels.i18n("invalid.password"), authenticate.getError());
            return false;
        }
        return true;
    }

}
