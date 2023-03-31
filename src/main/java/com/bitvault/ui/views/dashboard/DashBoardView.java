package com.bitvault.ui.views.dashboard;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.util.Labels;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignL.LOGOUT;

public final class DashBoardView extends BitVaultVBox {

    final DashBoardVM dashBoardVM;

    public static DashBoardView create(UserSession userSession) {
        DashBoardVM dashBoardVM = new DashBoardVM(userSession);
        return new DashBoardView(dashBoardVM);
    }

    DashBoardView(final DashBoardVM dashBoardVM) {

        this.dashBoardVM = dashBoardVM;
        init();
    }

    private void init() {

        FontIcon logOutIcon = new FontIcon(LOGOUT);

        final Button logout = new Button("", logOutIcon);
        logout.setAccessibleText(Labels.i18n("logout"));
        logout.setOnAction(event -> logout());

        final Tab passTab = new Tab(Labels.i18n("passwords"), dashBoardVM.getPasswordView());
        passTab.setClosable(false);

        final Tab cardTab = new Tab(Labels.i18n("cards"), new BitVaultVBox());
        cardTab.setClosable(false);

        final TabPane tabPane = new TabPane(passTab, cardTab);

        this.getChildren().addAll(logout, tabPane);

        JavaFxUtil.vGrowAlways(this);

    }

    private void logout() {
        final Stage stage = (Stage) this.getScene().getWindow();
        ViewLoader.load(stage, WelcomeView::new);
    }


}
