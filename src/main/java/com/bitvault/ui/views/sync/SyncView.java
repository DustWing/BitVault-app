package com.bitvault.ui.views.sync;

import com.bitvault.security.AesEncryptionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.services.factory.TestServiceFactory;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SyncView extends VBox {

    private final SyncViewModel syncViewModel;

    private final TextArea textArea;
    private final ImageView imageView;

    final TableView<SyncValue<Password>> tableView;


    public static SyncView createTest() {

        final String username = "Test";
        final String password = "Test";

        final EncryptionProvider encryptionProvider = new AesEncryptionProvider(password.toCharArray());
        final ServiceFactory serviceFactory = new TestServiceFactory(encryptionProvider);
        final UserSession userSession = new UserSession(username, encryptionProvider, serviceFactory);
        final SyncViewModel syncViewModel1 = new SyncViewModel(userSession);
        return new SyncView(syncViewModel1);
    }

    public SyncView(SyncViewModel syncViewModel) {
        this.syncViewModel = syncViewModel;

        ButtonBar buttonBar = createButtonBar();

        textArea = new TextArea();
        this.syncViewModel.logTestProperty().addListener((observable, oldValue, newValue) ->
                textArea.appendText(newValue)
        );
        ScrollPane scrollPane = addScrollPane(textArea);

        tableView = createTable(this.syncViewModel.getPasswords());
        Tab tab = new Tab("Password", tableView);
        tab.setClosable(false);
        Tab tabc = new Tab("Card", new TableView<String>());
        tabc.setClosable(false);
        TabPane tabPane = new TabPane(tab, tabc);

        this.imageView = new ImageView();

        this.getChildren().addAll(
                imageView,
                tabPane,
                scrollPane,
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

        final BvButton clearLog = new BvButton(Labels.i18n("clear"))
                .action(event -> clear())
                .defaultButton(false)
                .withDefaultSize();

        final BvButton showCache = new BvButton("Show Cache")
                .action(event -> this.syncViewModel.showCache())
                .defaultButton(false)
                .withDefaultSize();

        final ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons()
                .addAll(start, stop, clearLog, showCache);

        return buttonBar;
    }

    private ScrollPane addScrollPane(TextArea textArea) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }


    private TableView<SyncValue<Password>> createTable(ObservableList<SyncValue<Password>> passwords) {
        return new PasswordSyncTable(passwords);
    }

    public void start() {
        Result<Integer> portResult = this.syncViewModel.startServer();

        if (portResult.isFail()) {
            ErrorAlert.show("Error SyncView", portResult.getError());
        }

        Result<Image> qrdImage = this.syncViewModel.createQrdImage(portResult.get());
        if (qrdImage.isFail()) {
            ErrorAlert.show("Error SyncView Image", portResult.getError());
        }
        this.imageView.setImage(qrdImage.get());
    }


    public void stop() {
        this.syncViewModel.stopServer();

    }

    public void clear() {
        this.textArea.clear();
    }

}
