package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.grid.BvSimpleGrid;
import com.bitvault.ui.components.grid.GridRow;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.util.DateTimeUtils;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CONTENT_COPY;


public class PasswordTableRowFactory extends TableRow<Password> {


    private final PasswordVM passwordVM;

    private final Runnable onCopy;

    public PasswordTableRowFactory(PasswordVM passwordVM, Runnable onCopy) {
        super();
        this.passwordVM = passwordVM;
        this.onCopy = onCopy;
    }

    @Override
    protected void updateItem(Password item, boolean empty) {
        super.updateItem(item, empty);

        //do nothing
        if (item == null || empty) {
            return;
        }

        addContextMenu(this);
    }


    private void addContextMenu(TableRow<Password> row) {

        Password selectedItem = row.getItem();

        MenuItem copyUserName = new MenuItem(Labels.i18n("copy.username"));
        copyUserName.setOnAction(event -> copyUsername(selectedItem));

        MenuItem copyPassword = new MenuItem(Labels.i18n("copy.password"));
        copyPassword.setOnAction(event -> copyPassword(selectedItem));

        MenuItem edit = new MenuItem(Labels.i18n("edit"));
        edit.setOnAction(event -> showEditPopUp(selectedItem));

        MenuItem detail = new MenuItem(Labels.i18n("details"));
        detail.setOnAction(event -> showDetailsPopup(selectedItem));

        MenuItem delete = new MenuItem(Labels.i18n("delete"));
        delete.setOnAction(event -> delete(selectedItem));

        MenuItem duplicate = new MenuItem(Labels.i18n("duplicate"));
        duplicate.setOnAction(event -> duplicate(selectedItem));

        final ContextMenu contextMenu = new ContextMenu(copyUserName, copyPassword, edit, detail, delete, duplicate);

        row.setContextMenu(contextMenu);
    }

    public void delete(Password password) {
        passwordVM.delete(password);
    }


    private void copyPassword(Password selectedItem) {
        final boolean copied = passwordVM.copyPassword(selectedItem);
        if (copied) onCopy.run();
    }

    private void copyUsername(Password selectedItem) {
        final boolean copied = passwordVM.copyUsername(selectedItem);
        if (copied) onCopy.run();
    }

    private void duplicate(Password selectedItem) {
        this.passwordVM.create(selectedItem);
    }

    public void showEditPopUp(Password oldPass) {

        Result<Password> passwordResult = this.passwordVM.prepareForEdit(oldPass);
        if (passwordResult.hasError()) {
            return;
        }

        Password password = passwordResult.get();

        final PasswordDetailsView view = PasswordDetailsView.editPassword(
                password,
                new ArrayList<>(this.passwordVM.getCategoriesList()),
                oldPass.getSecureDetails().getProfile(),//use old profile
                this.passwordVM::update
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("edit.password")).show();
    }

    private void showDetailsPopup(Password selectedItem) {

        GridRow titleRow = new GridRow(List.of(
                new Label(Labels.i18n("title")), new Label(selectedItem.getSecureDetails().getTitle())
        ));


        Button usernameBtn = new BvButton("", new FontIcon(CONTENT_COPY))
                .action(event -> this.passwordVM.copyUsername(selectedItem));

        GridRow usernameRow = new GridRow(List.of(
                new Label(Labels.i18n("username")), new Label(selectedItem.getSecureDetails().getTitle()), usernameBtn
        ));

        Button passwordBtn = new BvButton("", new FontIcon(CONTENT_COPY))
                .action(event -> this.passwordVM.copyPassword(selectedItem));

        GridRow passwordRow = new GridRow(List.of(
                new Label(Labels.i18n("password")), new Label("********"), passwordBtn
        ));

        GridRow domain = new GridRow(List.of(
                new Label(Labels.i18n("domain")), new Label(selectedItem.getSecureDetails().getDomain())
        ));

        Text descrText = new Text(selectedItem.getSecureDetails().getDescription());
        descrText.setWrappingWidth(300);

        GridRow description = new GridRow(List.of(
                new Label(Labels.i18n("description")), descrText)
        );

        GridRow category = new GridRow(List.of(
                new Label(Labels.i18n("category")), new Label(selectedItem.getSecureDetails().getCategory().name())
        ));

        String expiresOn = DateTimeUtils.formatNoTime(selectedItem.getSecureDetails().getExpiresOn());
        GridRow expiry = new GridRow(List.of(
                new Label(Labels.i18n("expires.on")), new Label(expiresOn)
        ));

        String createdOn = DateTimeUtils.format(selectedItem.getSecureDetails().getCreatedOn());
        GridRow created = new GridRow(List.of(
                new Label(Labels.i18n("created.on")), new Label(createdOn)
        ));

        String updatedOn = DateTimeUtils.format(selectedItem.getSecureDetails().getModifiedOn());
        GridRow updated = new GridRow(List.of(
                new Label(Labels.i18n("modified.on")), new Label(updatedOn)
        ));

        GridRow masterP = new GridRow(List.of(
                new Label(Labels.i18n("master.password")), new Label(selectedItem.getSecureDetails().isRequiresMp() ? "Y" : "N")
        ));

        GridRow fav = new GridRow(List.of(
                new Label(Labels.i18n("favourite")), new Label(selectedItem.getSecureDetails().isFavourite() ? "Y" : "N")
        ));

        GridRow shared = new GridRow(List.of(
                new Label(Labels.i18n("shared")), new Label(selectedItem.getSecureDetails().isShared() ? "Y" : "N")
        ));


        BvSimpleGrid grid = new BvSimpleGrid(List.of(
                titleRow,
                usernameRow,
                passwordRow,
                domain,
                description,
                category,
                expiry,
                created,
                updated,
                masterP,
                fav,
                shared
        ));


        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);

        VBox vBox = new VBox(scrollPane);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(BvInsets.all10);

        ViewLoader.popUp(this.getScene().getWindow(), vBox, Labels.i18n("details")).show();

    }


}
