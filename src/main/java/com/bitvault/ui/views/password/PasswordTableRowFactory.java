package com.bitvault.ui.views.password;

import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;

import java.util.ArrayList;

import static com.bitvault.ui.views.password.PasswordDetailsPopUp.showDetailsPopup;


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
            setText(null);
            setGraphic(null);
            return;
        }

        addContextMenu(this);

    }

    private void addContextMenu(TableRow<Password> row) {

        Password selectedItem = row.getItem();

        MenuItem copyUserName = new MenuItem(Labels.i18n("copy.username"));
        copyUserName.setOnAction(event -> copyUsername(selectedItem.getUsername()));

        MenuItem copyPassword = new MenuItem(Labels.i18n("copy.password"));
        copyPassword.setOnAction(event -> copyPassword(selectedItem.getPassword()));

        MenuItem edit = new MenuItem(Labels.i18n("edit"));
        edit.setOnAction(event -> showEditPopUp(selectedItem));

        MenuItem detail = new MenuItem(Labels.i18n("details"));
        detail.setOnAction(event -> showDetailsPopup(
                        this.getScene(),
                        selectedItem,
                        this::copyUsername,
                        this::copyPassword
                )
        );

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


    private void copyPassword(String password) {
        final boolean copied = passwordVM.copyPassword(password);
        if (copied) onCopy.run();
    }

    private void copyUsername(String username) {
        final boolean copied = passwordVM.copyUsername(username);
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
                this.passwordVM.getPassLength(),
                this.passwordVM::update
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("edit.password")).show();
    }


}
