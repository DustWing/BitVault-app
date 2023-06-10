package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BitVaultHBox;
import com.bitvault.ui.components.TimerBar;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.KeyCombinationConst;
import com.bitvault.util.Labels;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

import static com.bitvault.ui.views.password.PasswordDetailsPopUp.showDetailsPopup;

public class PasswordTableView extends BorderPane {

    private final PasswordVM passwordVM;
    private final TimerBar timerBar;

    public PasswordTableView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        final TableView<Password> tableView = createTable();

        this.timerBar = new TimerBar(new ProgressBar(), JavaFxUtil::clearClipBoard);
        timerBar.getProgressBar().setVisible(false);

        final BitVaultHBox bottomHBox = new BitVaultHBox(timerBar.getProgressBar())
                .maxH(20);

        this.setCenter(tableView);
        this.setBottom(bottomHBox);
    }

    private TableView<Password> createTable() {

        TableView<Password> tableView = new TableView<>(this.passwordVM.getFilteredList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setRowFactory(param -> new PasswordTableRowFactory(this.passwordVM, () -> this.timerBar.start(30)));

        final TableColumn<Password, String> titleC = new TableColumn<>(Labels.i18n("title"));
        titleC.setCellValueFactory(cellData -> cellData.getValue().getSecureDetails().titleProperty());

        final TableColumn<Password, String> userNameC = new TableColumn<>(Labels.i18n("username"));
        userNameC.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        final TableColumn<Password, String> descriptionC = new TableColumn<>(Labels.i18n("description"));
        descriptionC.setCellValueFactory(cellData -> cellData.getValue().getSecureDetails().descriptionProperty());

        final TableColumn<Password, String> domainC = new TableColumn<>(Labels.i18n("domain"));
        domainC.setCellValueFactory(cellData -> cellData.getValue().domainProperty());
        domainC.setCellFactory(new HyperLinkCell<>());

        final TableColumn<Password, Category> categoryC = new TableColumn<>(Labels.i18n("category"));
        categoryC.setCellValueFactory(cellData -> cellData.getValue().getSecureDetails().categoryProperty());
        categoryC.setCellFactory(param -> new CategoryCellFactory<>());

        tableView.getColumns().add(titleC);
        tableView.getColumns().add(userNameC);
        tableView.getColumns().add(descriptionC);
        tableView.getColumns().add(domainC);
        tableView.getColumns().add(categoryC);
        tableView.getStyleClass().add(BvStyles.EDGE_TO_EDGE);

        setTableEvents(tableView);

        return tableView;
    }

    private void setTableEvents(TableView<Password> tableView) {
        tableView.setOnKeyPressed(event -> {

            final Password selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            if (KeyCombinationConst.ctrlC.match(event)) {
                copyPassword(selectedItem.getPassword());
            }

            if (KeyCombinationConst.ctrlB.match(event)) {
                copyUsername(selectedItem.getUsername());
            }

            if (KeyCode.DELETE.equals(event.getCode())) {
                deleteRecord(selectedItem);
            }
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                showDetailsDialog(tableView);
            }
        });
    }

    private void showDetailsDialog(TableView<Password> tableView) {
        final Password selectedItem = tableView.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            return;
        }

        showDetailsPopup(
                this.getScene(),
                selectedItem,
                this::copyUsername,
                this::copyPassword
        );
    }

    private void copyPassword(String password) {
        final boolean copied = passwordVM.copyPassword(password);
        if (copied)
            this.timerBar.start(30);
    }

    private void copyUsername(final String username) {
        final boolean copied = passwordVM.copyUsername(username);
        if (copied)
            this.timerBar.start(30);
    }

    private void deleteRecord(final Password selectedItem) {
        passwordVM.delete(selectedItem);
    }


}
