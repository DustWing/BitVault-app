package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BitVaultHBox;
import com.bitvault.ui.components.TimerBar;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.KeyCombinationConst;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

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
        tableView.setRowFactory(param -> new PasswordTableRowFactory(
                        this::copyUsername,
                        this::copyPassword,
                        this::showEditPopUp,
                        this::delete,
                        this.passwordVM::create
                )
        );

        final TableColumn<Password, String> titleC = new TableColumn<>("Title");
        titleC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getTitle()));

        final TableColumn<Password, String> userNameC = new TableColumn<>("User Name");
        userNameC.setCellValueFactory(new PropertyValueFactory<>("username"));

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

    public void showEditPopUp(Password oldPass) {

        Result<Password> passwordResult = passwordVM.prepareForEdit(oldPass);
        if (passwordResult.hasError()) {
            return;
        }

        Password password = passwordResult.get();

        final PasswordDetailsView view = PasswordDetailsView.editPassword(
                password,
                new ArrayList<>(passwordVM.getCategoriesList()),
                oldPass.getSecureDetails().getProfile(),//use old profile
                passwordVM::update
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("edit.password")).show();
    }

    public void delete(Password password) {
        passwordVM.delete(password);
    }

    private void copyPassword(Password selectedItem) {
        final boolean copied = passwordVM.copyPassword(selectedItem);
        if (copied)
            this.timerBar.start(30);
    }

    private void copyPassword(TableView<Password> tableView) {
        final Password selectedItem = tableView.getSelectionModel().getSelectedItem();
        final boolean copied = passwordVM.copyPassword(selectedItem);
        if (copied)
            this.timerBar.start(30);
    }

    private void copyUsername(Password selectedItem) {
        final boolean copied = passwordVM.copyUsername(selectedItem);
        if (copied)
            this.timerBar.start(30);
    }


}
