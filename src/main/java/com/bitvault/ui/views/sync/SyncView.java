package com.bitvault.ui.views.sync;

import com.bitvault.services.cached.PasswordServiceCached;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SyncView extends VBox {

    private final SyncViewModel syncViewModel;

    private final TextArea textArea;
    private final ImageView imageView;

    final TableView<Password> tableView;


    public static SyncView createTest() {

        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        Category category = new Category(
                id,
                "Default",
                "#FFFFFF",
                LocalDateTime.now(),
                null,
                "PASSWORD"
        );

        SecureDetails secureDetails = new SecureDetails(
                id,
                category,
                null,
                "www.domain.com",
                "title",
                "description",
                false,
                now,
                null,
                null,
                null,
                false,
                false
        );

        Password password = new Password(
                id,
                "MyUserName",
                "MyPassword",
                secureDetails
        );

        IPasswordService passwordService = () -> Result.ok(List.of(password));

        IPasswordService passwordServiceCached = new PasswordServiceCached(passwordService);

        return new SyncView(new SyncViewModel(passwordServiceCached));
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

        BvButton showCache = new BvButton("Show Cache")
                .action(event -> this.syncViewModel.showCache())
                .defaultButton(false)
                .withDefaultSize();

        ButtonBar buttonBar = new ButtonBar();
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


    private TableView<Password> createTable(ObservableList<Password> passwords) {

        TableView<Password> tableView = new TableView<>(passwords);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<Password, String> titleC = new TableColumn<>("Title");
        titleC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getTitle()));

        final TableColumn<Password, String> userNameC = new TableColumn<>("Username");
        userNameC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUsername()));

        final TableColumn<Password, String> descriptionC = new TableColumn<>("Description");
        descriptionC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getDescription()));

        final TableColumn<Password, String> domainC = new TableColumn<>("Domain");
        domainC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUrl()));
        domainC.setCellFactory(new HyperLinkCell<>());

        tableView.getColumns().add(titleC);
        tableView.getColumns().add(userNameC);
        tableView.getColumns().add(descriptionC);
        tableView.getColumns().add(domainC);

        tableView.getStyleClass().add(BvStyles.EDGE_TO_EDGE);

        return tableView;
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
