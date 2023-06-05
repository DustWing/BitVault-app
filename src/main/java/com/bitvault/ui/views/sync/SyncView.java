package com.bitvault.ui.views.sync;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SyncView extends VBox {

    private final SyncViewModel syncViewModel;
    private final ImageView imageView;
    final TableView<SyncValue<Password>> tableView;


    public static SyncView createTest() {
        final UserSession userSession = UserSession.createTest();
        final SyncViewModel syncViewModel1 = new SyncViewModel(userSession);
        return new SyncView(syncViewModel1);
    }

    public static SyncView create(UserSession userSession) {
        final SyncViewModel syncViewModel1 = new SyncViewModel(userSession);
        return new SyncView(syncViewModel1);
    }

    public SyncView(SyncViewModel syncViewModel) {
        this.syncViewModel = syncViewModel;

        ButtonBar buttonBar = createButtonBar();

        tableView = createTable();
        Tab tab = new Tab("Password", tableView);
        tab.setClosable(false);

        Tab tabc = new Tab("Card", new TableView<String>());
        tabc.setClosable(false);
        TabPane tabPane = new TabPane(tab, tabc);

        this.imageView = new ImageView();

        this.getChildren().addAll(
                imageView,
                tabPane,
                buttonBar
        );

        setAlignment(Pos.CENTER);
        JavaFxUtil.vGrowAlways(this);
    }

    private ButtonBar createButtonBar() {
        final BvButton start = new BvButton(Labels.i18n("start"))
                .action(event -> start())
                .defaultButton(true)
                .withDefaultSize();

        final BvButton stop = new BvButton(Labels.i18n("stop"))
                .action(event -> stop())
                .defaultButton(false)
                .withDefaultSize();

        final ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons()
                .addAll(start, stop);

        return buttonBar;
    }

    private TableView<SyncValue<Password>> createTable() {
        return PasswordSyncTable.createTable(
                this.syncViewModel.getPasswords(),
                this.syncViewModel.getCategories(),
                this.syncViewModel.getEncryptionProvider()
        );
    }

    public void start() {
        Result<Integer> portResult = this.syncViewModel.startServer();

        if (portResult.hasError()) {
            ErrorAlert.show("Error SyncView", portResult.getError());
        }

        Result<Image> qrdImage = this.syncViewModel.createQrdImage(portResult.get());
        if (qrdImage.hasError()) {
            ErrorAlert.show("Error SyncView Image", portResult.getError());
        }
        this.imageView.setImage(qrdImage.get());
    }


    public void stop() {
        this.syncViewModel.stopServer();
    }
}
