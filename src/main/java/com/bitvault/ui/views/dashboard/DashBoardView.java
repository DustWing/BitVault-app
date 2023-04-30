package com.bitvault.ui.views.dashboard;

import com.bitvault.security.UserSession;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.ui.views.password.PasswordView;
import com.bitvault.util.Labels;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignL.LOGOUT;

public final class DashBoardView extends BorderPane {

    final DashBoardVM dashBoardVM;

    public static DashBoardView create(UserSession userSession) {
        DashBoardVM dashBoardVM = new DashBoardVM(userSession);
        return new DashBoardView(dashBoardVM);
    }

    DashBoardView(final DashBoardVM dashBoardVM) {
        this.dashBoardVM = dashBoardVM;

        final Button logout = getLogoutBtn();
        final PasswordView passwordView = getPasswordView();

        this.setTop(logout);
        this.setCenter(passwordView);
    }


    private Button getLogoutBtn() {

        FontIcon logOutIcon = new FontIcon(LOGOUT);
        final Button logout = new Button(Labels.i18n("logout"), logOutIcon);
        logout.setAccessibleText(Labels.i18n("logout"));
        logout.setOnAction(event -> logout());
        BorderPane.setAlignment(logout, Pos.CENTER_RIGHT);
        BorderPane.setMargin(logout, new Insets(10,10,20,0));
        return logout;
    }

    private void logout() {
        final Stage stage = (Stage) this.getScene().getWindow();
        ViewLoader.load(stage, 840, 600, WelcomeView::new);
    }

    private PasswordView getPasswordView() {
        final PasswordVM passwordVM = new PasswordVM(dashBoardVM.getUserSession(), dashBoardVM.getSelectedProfile());
        return new PasswordView(passwordVM);
    }

}
