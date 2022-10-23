package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.util.Labels;
import com.bitvault.util.ResourceLoader;
import com.bitvault.ui.viewmodel.DashBoardVM;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import static com.bitvault.util.Theme.DARK;

public final class DashBoardView extends BitVaultVBox {

    final DashBoardVM dashBoardVM;

    public DashBoardView(
            final DashBoardVM dashBoardVM
    ) {

        this.dashBoardVM = dashBoardVM;
        init();
    }

    private void init() {

        final BitVaultFlatButton logout = new BitVaultFlatButton(Labels.i18n("logout"));
        logout.setOnAction(event -> {

            final Stage stage = (Stage) this.getScene().getWindow();
            stage.setWidth(640);
            stage.setHeight(400);
            stage.centerOnScreen();

            final WelcomeView view = new WelcomeView();
            final Scene scene = new Scene(view, 640, 400);
            scene.getStylesheets().add(ResourceLoader.loadURL(DARK).toExternalForm());
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

        super.vGrowAlways();
    }


}
