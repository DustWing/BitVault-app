package com.bitvault.ui.views.password;

import com.bitvault.ui.components.*;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.*;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLUS;

public class PasswordTableView extends BorderPane {

    private final PasswordVM passwordVM;
    private final TimerBar timerBar;


    private final FilteredList<Password> filteredList;

    public PasswordTableView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        ObservableList<Password> passwords = passwordVM.getPasswords();

        this.filteredList = new FilteredList<>(passwords, s -> true);
        final TableView<Password> tableView = createTable(filteredList);

        VBox vBox = new CardBuilder().add(tableView).build();

        final HBox topBar = topBar(filteredList);
        BorderPane.setMargin(topBar, BvInsets.bottom10);

        this.timerBar = new TimerBar(new ProgressBar(), JavaFxUtil::clearClipBoard);
        timerBar.getProgressBar().setVisible(false);

        final BitVaultHBox bottomHBox = new BitVaultHBox(timerBar.getProgressBar())
                .maxH(20);

        this.setTop(topBar);
        this.setCenter(vBox);
        this.setBottom(bottomHBox);

        this.setOnKeyPressed(event -> {
            if (KeyCombinationConst.ctrlC.match(event)) {
                copyPassword(tableView);
            }
        });
    }

    private BvTextField createSearchTf(FilteredList<Password> filteredList) {

        return new BvTextField()
                .withPromptText(Labels.i18n("search"))
                .withMediumSize()
                .filter(filteredList, Password::contains);
    }

    private TextColorComboBox<Category> categoryDropdown() {
        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withCircle(this.passwordVM.getCategories());
        categoriesDd.setValue(this.passwordVM.getFakeCategory());
        return categoriesDd;
    }


    private HBox topBar(FilteredList<Password> filteredList) {
        final FontIcon plusIcon = new FontIcon(PLUS);

        final Button addNewBtn = new BvButton(Labels.i18n("add.new"), plusIcon)
                .withDefaultSize()
                .action(event -> showNewPassPopUp());
        addNewBtn.setTooltip(new Tooltip(Labels.i18n("add.new.password")));

        final TextColorComboBox<Category> categoriesDd = categoryDropdown();

        categoriesDd.setOnAction(event -> {
                    Category selectedItem = categoriesDd.getValue();
                    if (selectedItem != null) {
                        this.filterByCategory(selectedItem.id());
                    }
                }
        );
        JavaFxUtil.mediumSize(categoriesDd);


        TextField searchTf = createSearchTf(filteredList);
        VBox.setMargin(searchTf, BvInsets.right15);

        final HBox rightBox = new BitVaultHBox(addNewBtn);
        rightBox.setAlignment(Pos.CENTER_LEFT);

        final BitVaultHBox leftBox = new BitVaultHBox(categoriesDd, searchTf);
        leftBox.setAlignment(Pos.CENTER_RIGHT);
        leftBox.setSpacing(BvSpacing.SMALL);
        return new BitVaultHBox(rightBox, leftBox);
    }

    private TableView<Password> createTable(ObservableList<Password> passwords) {

        TableView<Password> tableView = new TableView<>(passwords);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setRowFactory(param -> new PasswordTableRowFactory(
                this::copyUsername,
                this::copyPassword,
                this::showEditPopUp,
                this::delete
        ));

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

    private void showNewPassPopUp() {

        final PasswordDetailsView view = PasswordDetailsView.newPassword(
                new ArrayList<>(passwordVM.getCategoriesList()),
                passwordVM.getProfile(),
                passwordVM::create
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("add.new.password")).show();
    }

    public void showEditPopUp(Password oldPass) {

        Result<Password> passwordResult = passwordVM.prepareForEdit(oldPass);
        if(passwordResult.hasError()){
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

    public void filterByCategory(String id) {
        if ("ALL".equals(id)) {
            this.filteredList.setPredicate(password -> true);
            return;
        }
        this.filteredList.setPredicate(password -> password.getSecureDetails().getCategory().id().equals(id));
    }
}
