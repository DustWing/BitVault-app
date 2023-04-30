package com.bitvault.ui.views.password;

import com.bitvault.ui.model.Password;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;

import java.util.function.Consumer;

public class PasswordTableRowFactory extends TableRow<Password> {


    private final Consumer<Password> onCopyUserName;
    private final Consumer<Password> onCopyPassword;
    private final Consumer<Password> onEdit;
    private final Consumer<Password> onDelete;

    public PasswordTableRowFactory(Consumer<Password> onCopyUserName, Consumer<Password> onCopyPassword, Consumer<Password> onEdit, Consumer<Password> onDelete) {
        super();
        this.onCopyUserName = onCopyUserName;
        this.onCopyPassword = onCopyPassword;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
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

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUserName = new MenuItem("Copy UserName");
        copyUserName.setOnAction(event -> onCopyUserName.accept(selectedItem));

        MenuItem copyPassword = new MenuItem("Copy Password");
        copyPassword.setOnAction(event -> onCopyPassword.accept(selectedItem));

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(event -> onEdit.accept(selectedItem));

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> onDelete.accept(selectedItem));

        contextMenu.getItems().addAll(copyUserName, copyPassword, edit, delete);

        row.setContextMenu(contextMenu);
    }


}
