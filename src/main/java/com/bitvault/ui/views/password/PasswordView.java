package com.bitvault.ui.views.password;

import com.bitvault.ui.components.*;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.KeyCombinationConst;
import com.bitvault.ui.views.PasswordDetailsView;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.util.Labels;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


public class PasswordView extends BitVaultVBox {

    private final PasswordVM passwordVM;

    private final TimerBar timerBar;

    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        ObservableList<Password> passwords = passwordVM.getPasswords();

        final FilteredList<Password> filteredList = new FilteredList<>(passwords, s -> true);

        TableView<Password> tableView = createTable(filteredList);


        BvTextField searchTf = createSearchTf(filteredList);
        VBox.setMargin(searchTf, BvInsets.right15);


        final BitVaultFlatButton addNewBtn = new BitVaultFlatButton(Labels.i18n("add.new"))
                .action(event -> showNewPassPopUp());


        this.timerBar = new TimerBar(new ProgressBar(), passwordVM::onTimeEnd);
        timerBar.getProgressBar().setVisible(false);

        final BitVaultFlatButton reloadBtn = new BitVaultFlatButton(Labels.i18n("reload"))
                .action(event -> passwordVM.reload());

        final BitVaultHBox topHBox = new BitVaultHBox(
                addNewBtn,
                reloadBtn,
                searchTf

        ).maxH(100);

        final BitVaultHBox bottomHBox = new BitVaultHBox(
                timerBar.getProgressBar()
        ).maxH(20);


        this.getChildren().addAll(
                topHBox,
                tableView,
                bottomHBox
        );


        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        super.vGrowAlways();

        this.setOnKeyPressed(event -> {
            if (KeyCombinationConst.ctrlC.match(event)) {
                copyPassword(tableView);
            }
        });

    }


    private TableView<Password> createTable(ObservableList<Password> passwords) {

        TableView<Password> tableView = new TableView<>(passwords);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<Password, String> userNameC = new TableColumn<>("User Name");

        userNameC.setCellValueFactory(new PropertyValueFactory<>("username"));

        final TableColumn<Password, String> passwordC = new TableColumn<>("Description");

        passwordC.setCellValueFactory(new PropertyValueFactory<>("secureDetails.description"));
        passwordC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSecureDetails().getDescription()));


        tableView.getColumns().add(userNameC);
        tableView.getColumns().add(passwordC);

        setContextMenu(tableView);

        return tableView;
    }

    private void setContextMenu(TableView<Password> tableView) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUserName = new MenuItem("Copy UserName");
        copyUserName.setOnAction(event -> {

                    Password selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem == null) return;
                    System.out.println(selectedItem);
                }
        );
        MenuItem copyPassword = new MenuItem("Copy Password");
        copyPassword.setOnAction(event -> copyPassword(tableView));

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(
                event -> {
                    Password selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem == null) return;
                    showEditPopUp(selectedItem);
                }
        );

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(
                event -> {
                    Password selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem == null) return;
                    delete(selectedItem);
                }
        );

        contextMenu.getItems().addAll(copyUserName, copyPassword, edit, delete);

        tableView.setContextMenu(contextMenu);
    }


    private BvTextField createSearchTf(FilteredList<Password> filteredList) {

        return new BvTextField()
                .withPromptText(Labels.i18n("search"))
                .filter(filteredList, (password, s) -> {
                            var toLower = s.toLowerCase();
                            return password.getUsername().toLowerCase().contains(toLower)
                                    || password.getSecureDetails().getDomain().toLowerCase().contains(toLower);
                        }
                );
    }


    private void showNewPassPopUp() {

        final PasswordDetailsView view = PasswordDetailsView.create(
                passwordVM.getCategories(),
                passwordVM.getProfile(),
                passwordVM::create
        );

        JavaFxUtil.popUp(this.getScene().getWindow(), view).show();
    }


    public void showEditPopUp(Password oldPass) {
        final PasswordDetailsView view = PasswordDetailsView.edit(
                oldPass,
                passwordVM.getCategories(),
                passwordVM.getProfile(),
                passwordVM::update
        );

        JavaFxUtil.popUp(this.getScene().getWindow(), view).show();
    }

    public void delete(Password password) {
        passwordVM.delete(password);
    }


    private void copyPassword(TableView<Password> tableView) {
        final Password selectedItem = tableView.getSelectionModel().getSelectedItem();
        final boolean copied = passwordVM.copyPassword(selectedItem);
        if(copied)
            this.timerBar.start(30);
    }
}
