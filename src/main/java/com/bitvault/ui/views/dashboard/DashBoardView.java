package com.bitvault.ui.views.dashboard;

import com.bitvault.BitVault;
import com.bitvault.security.UserSession;
import com.bitvault.ui.async.AsyncTask;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.ui.views.password.PasswordView;
import com.bitvault.util.Labels;
import com.bitvault.util.ResourceLoader;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.COG;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignH.HOME;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignL.LOGOUT;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignM.MENU;


public final class DashBoardView extends BorderPane {

    private final DashBoardVM dashBoardVM;

    private final MenuBar menuBar;

    private Button button;

    public static DashBoardView create(UserSession userSession) {
        DashBoardVM dashBoardVM = new DashBoardVM(userSession);
        return new DashBoardView(dashBoardVM);
    }

    DashBoardView(final DashBoardVM dashBoardVM) {
        this.dashBoardVM = dashBoardVM;
        this.menuBar = menuBar();

        final URL iconUrl = ResourceLoader.loadURL("/com.bitvault/icons/256moth.png");

        button = new BvButton("", new ImageView(iconUrl.toExternalForm()))
                .action(event -> createPasswordView());

        this.setTop(menuBar);
        this.setCenter(button);
    }


    private MenuBar menuBar() {

        final FontIcon menuIcon = new FontIcon(MENU);
        final Menu menu = new Menu("", menuIcon);

        final FontIcon homeIcon = new FontIcon(HOME);
        final MenuItem home = new MenuItem(Labels.i18n("home"), homeIcon);
        home.setOnAction(__ -> homeAction());
        menu.getItems().add(home);

        final FontIcon settingIcon = new FontIcon(COG);
        final MenuItem settings = new MenuItem(Labels.i18n("settings"), settingIcon);
        menu.getItems().add(settings);

        final FontIcon logOutIcon = new FontIcon(LOGOUT);
        final MenuItem logout = new MenuItem(Labels.i18n("logout"), logOutIcon);
        logout.setOnAction(event -> logout());
        menu.getItems().add(logout);

        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        return menuBar;
    }

    private void homeAction() {
        this.setCenter(button);
    }


    private void logout() {
        BitVault.runOnCloseActions();
        final Stage stage = (Stage) this.getScene().getWindow();
        ViewLoader.load(stage, 840, 600, WelcomeView::new);
    }

    private void createPasswordView() {

        AsyncTask.toRun(() -> {
                            final PasswordVM passwordVM = new PasswordVM(dashBoardVM.getUserSession());
                            return new PasswordView(passwordVM);
                        }
                ).onFailure(asyncTaskException -> ErrorAlert.show("DashBoard", asyncTaskException))
                .onSuccess(passwordView -> Platform.runLater(() -> this.setCenter(passwordView)))
                .start();

    }


}
