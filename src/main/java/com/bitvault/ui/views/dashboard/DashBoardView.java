package com.bitvault.ui.views.dashboard;

import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.util.Labels;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignL.LOGOUT;

public final class DashBoardView extends BitVaultVBox {

    final DashBoardVM dashBoardVM;

    public DashBoardView(
            final DashBoardVM dashBoardVM
    ) {

        this.dashBoardVM = dashBoardVM;
        init();
    }

    private void init() {

        FontIcon logOutIcon = new FontIcon(LOGOUT);

        final Button logout = new Button("", logOutIcon);
        logout.setAccessibleText(Labels.i18n("logout"));
        logout.setOnAction(event -> {

            final Stage stage = (Stage) this.getScene().getWindow();
            stage.setWidth(640);
            stage.setHeight(400);
            stage.centerOnScreen();

            final WelcomeView view = new WelcomeView();
            final Scene scene = new Scene(view, 640, 400);
            stage.setScene(scene);
        });


        final Tab passTab = new Tab(Labels.i18n("passwords"), dashBoardVM.getPasswordView());
        passTab.setClosable(false);

        final Tab cardTab = new Tab(Labels.i18n("cards"), new BitVaultVBox());
        cardTab.setClosable(false);

        final TabPane tabPane = new TabPane(passTab, cardTab);

        this.getChildren().addAll(
                logout,
                tabPane
        );

        JavaFxUtil.vGrowAlways(this);

    }


}
