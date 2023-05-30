package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BitVaultHBox;
import com.bitvault.ui.components.TimerBar;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.KeyCombinationConst;
import com.bitvault.util.Labels;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class PasswordTableView extends BorderPane {

    private final PasswordVM passwordVM;
    private final TimerBar timerBar;

    public PasswordTableView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        final TableView<Password> tableView = createTable(this.passwordVM.getFilteredList());

        this.timerBar = new TimerBar(new ProgressBar(), JavaFxUtil::clearClipBoard);
        timerBar.getProgressBar().setVisible(false);

        final BitVaultHBox bottomHBox = new BitVaultHBox(timerBar.getProgressBar())
                .maxH(20);

        this.setCenter(tableView);
        this.setBottom(bottomHBox);

        this.setOnKeyPressed(event -> {
            if (KeyCombinationConst.ctrlC.match(event)) {
                copyPassword(tableView);
            }
        });
    }

    private TableView<Password> createTable(ObservableList<Password> passwords) {

        TableView<Password> tableView = new TableView<>(passwords);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setRowFactory(param -> new PasswordTableRowFactory(this.passwordVM, () -> this.timerBar.start(30)));

        final TableColumn<Password, String> titleC = new TableColumn<>(Labels.i18n("title"));
        titleC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getTitle()));

        final TableColumn<Password, String> userNameC = new TableColumn<>(Labels.i18n("username"));
        userNameC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUsername()));

        final TableColumn<Password, String> descriptionC = new TableColumn<>(Labels.i18n("description"));
        descriptionC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getDescription()));

        final TableColumn<Password, String> domainC = new TableColumn<>(Labels.i18n("domain"));
        domainC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUrl()));
        domainC.setCellFactory(new HyperLinkCell<>());

        final TableColumn<Password, String> categoryC = new TableColumn<>(Labels.i18n("category"));
        categoryC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getCategory().name()));

        tableView.getColumns().add(titleC);
        tableView.getColumns().add(userNameC);
        tableView.getColumns().add(descriptionC);
        tableView.getColumns().add(domainC);
        tableView.getColumns().add(categoryC);
        tableView.getStyleClass().add(BvStyles.EDGE_TO_EDGE);

        return tableView;
    }


    private void copyPassword(TableView<Password> tableView) {
        final Password selectedItem = tableView.getSelectionModel().getSelectedItem();
        final boolean copied = passwordVM.copyPassword(selectedItem);
        if (copied)
            this.timerBar.start(30);
    }


}
