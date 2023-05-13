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

        if (requiresMp) {
            return userSession.authWithCoolDown(() -> authenticate(userSession, title, header));
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Labels.i18n("delete.context"));
        Optional<ButtonType> buttonType = alert.showAndWait();

        return buttonType.filter(ButtonType.OK::equals).isPresent();

    }

    public static boolean editAuthenticate(UserSession userSession, boolean requiresMp) {

        if (!requiresMp) {
            return true;
        }
        String title = Labels.i18n("edit.record");
        String header = Labels.i18n("edit.header");

        return userSession.authWithCoolDown(() -> authenticate(userSession, title, header));
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

        if (authenticate.isFail()) {
            ErrorAlert.show(Labels.i18n("invalid.password"), authenticate.getError());
            return false;
        }
        return true;
    }

}
