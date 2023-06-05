package com.bitvault.ui.views.dashboard;

import com.bitvault.security.UserSession;
import com.bitvault.ui.async.AsyncTask;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.BvSceneSize;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.ui.views.password.PasswordView;
import com.bitvault.ui.views.sync.SyncView;
import com.bitvault.ui.views.sync.SyncViewModel;
import com.bitvault.util.Labels;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.COG;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignH.HOME;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignL.LOGOUT;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignM.MENU;


public final class DashBoardView extends BorderPane {

    private final DashBoardVM dashBoardVM;

    private final TilePane tilePane;

    public static DashBoardView create(UserSession userSession) {
        DashBoardVM dashBoardVM = new DashBoardVM(userSession);
        return new DashBoardView(dashBoardVM);
    }

    DashBoardView(final DashBoardVM dashBoardVM) {
        this.dashBoardVM = dashBoardVM;
        MenuBar menuBar = menuBar();

        this.tilePane = createButtons();

        this.setTop(menuBar);
        this.setCenter(tilePane);
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


    private TilePane createButtons() {

        Button passBtn = new BvButton("Passwords")
                .action(event -> createPasswordView())
                .withDefaultSize();

        Button syncBtn = new BvButton("Sync")
                .action(event -> createSyncView())
                .withDefaultSize();

        TilePane tilePane = new TilePane();
        tilePane.getChildren().add(passBtn);
        tilePane.getChildren().add(syncBtn);

        tilePane.setTileAlignment(Pos.CENTER);
        tilePane.setHgap(10);
        tilePane.setVgap(10);

        BorderPane.setAlignment(tilePane, Pos.CENTER);
        BorderPane.setMargin(tilePane, BvInsets.all10);

        return tilePane;
    }

    private void homeAction() {
        this.setCenter(tilePane);
    }


    private void logout() {

        this.dashBoardVM.logout();

        final Stage stage = (Stage) this.getScene().getWindow();
        final BvSceneSize aDefault = BvSceneSize.Default;
        ViewLoader.load(stage, aDefault.width(), aDefault.height(), WelcomeView::new);
    }

    private void createPasswordView() {

        AsyncTask.toRun(() -> new PasswordView(new PasswordVM(dashBoardVM.getUserSession()))
                ).onFailure(asyncTaskException -> ErrorAlert.show("DashBoard", asyncTaskException))
                .onSuccess(passwordView -> Platform.runLater(() -> this.setCenter(passwordView)))
                .start();

    }

    private void createSyncView() {

        AsyncTask.toRun(() -> new SyncView(new SyncViewModel(this.dashBoardVM.getUserSession()))
                ).onFailure(asyncTaskException -> ErrorAlert.show("DashBoard", asyncTaskException))
                .onSuccess(passwordView -> Platform.runLater(() -> this.setCenter(passwordView)))
                .start();

    }


}
