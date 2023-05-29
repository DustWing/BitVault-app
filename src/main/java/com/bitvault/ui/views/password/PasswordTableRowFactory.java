package com.bitvault.ui.views.password;

import com.bitvault.ui.model.Password;
import com.bitvault.util.Labels;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;

import java.util.function.Consumer;

public class PasswordTableRowFactory extends TableRow<Password> {


    private final Consumer<Password> onCopyUserName;
    private final Consumer<Password> onCopyPassword;
    private final Consumer<Password> onEdit;
    private final Consumer<Password> onDelete;
    private final Consumer<Password> onDuplicate;

    public PasswordTableRowFactory(
            Consumer<Password> onCopyUserName,
            Consumer<Password> onCopyPassword,
            Consumer<Password> onEdit,
            Consumer<Password> onDelete,
            Consumer<Password> onDuplicate
    ) {
        super();
        this.onCopyUserName = onCopyUserName;
        this.onCopyPassword = onCopyPassword;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
        this.onDuplicate = onDuplicate;
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
        copyUserName.setOnAction(event -> onCopyUserName.accept(selectedItem));

        MenuItem copyPassword = new MenuItem(Labels.i18n("copy.password"));
        copyPassword.setOnAction(event -> onCopyPassword.accept(selectedItem));

        MenuItem edit = new MenuItem(Labels.i18n("edit"));
        edit.setOnAction(event -> onEdit.accept(selectedItem));

        MenuItem delete = new MenuItem(Labels.i18n("delete"));
        delete.setOnAction(event -> onDelete.accept(selectedItem));

        MenuItem duplicate = new MenuItem(Labels.i18n("duplicate"));
        duplicate.setOnAction(event -> onDuplicate.accept(selectedItem));

        final ContextMenu contextMenu = new ContextMenu(copyUserName, copyPassword, edit, delete, duplicate);

        row.setContextMenu(contextMenu);
    }


}
