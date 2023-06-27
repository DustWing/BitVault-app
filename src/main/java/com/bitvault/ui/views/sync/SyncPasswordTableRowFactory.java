package com.bitvault.ui.views.sync;

import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.password.PasswordDetailsView;
import com.bitvault.util.Labels;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.function.Consumer;

import static com.bitvault.ui.views.password.PasswordDetailsPopUp.showDetailsPopup;

public class SyncPasswordTableRowFactory extends TableRow<SyncValue<Password>> {


    private final List<Category> categories;
    private final Consumer<Password> onSave;

    private final int passLength;


    public SyncPasswordTableRowFactory(
            List<Category> categories,
            final int passLength,
            Consumer<Password> onSave
    ) {
        super();
        this.categories = categories;
        this.passLength = passLength;
        this.onSave = onSave;
    }

    @Override
    protected void updateItem(SyncValue<Password> item, boolean empty) {
        super.updateItem(item, empty);

        //do nothing
        if (item == null || empty) {
            return;
        }

        addContextMenu(this);

        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(item.warningMsgProperty());
        this.setTooltip(tooltip);
    }


    private void addContextMenu(TableRow<SyncValue<Password>> row) {

        final ContextMenu contextMenu = new ContextMenu();
        SyncValue<Password> selectedItem = row.getItem();

        MenuItem details = new MenuItem(Labels.i18n("details"));
        details.setOnAction(event -> showDetails(selectedItem.getNewValue()));
        contextMenu.getItems().add(details);

        MenuItem edit = new MenuItem(Labels.i18n("edit"));
        edit.setOnAction(event -> showEditPopUp(selectedItem));
        contextMenu.getItems().add(edit);

        if (SyncValue.ActionState.REQUIRED.equals(selectedItem.getActionState())
                || SyncValue.ActionState.DISCARD.equals(selectedItem.getActionState())) {

            MenuItem acceptNew = new MenuItem(Labels.i18n("accept.new"));
            acceptNew.setOnAction(event -> selectedItem.actionStateProperty().set(SyncValue.ActionState.SAVE));
            contextMenu.getItems().add(acceptNew);
        }

        if (!SyncValue.ActionState.DISCARD.equals(selectedItem.getActionState())) {
            MenuItem discard = new MenuItem(Labels.i18n("discard"));
            discard.setOnAction(
                    event -> selectedItem.actionStateProperty().set(SyncValue.ActionState.DISCARD)
            );
            contextMenu.getItems().add(discard);
        }

        row.setContextMenu(contextMenu);
    }

    public void showDetails(Password password) {
        showDetailsPopup(
                this.getScene(),
                password,
                JavaFxUtil::copyToClipBoard,
                JavaFxUtil::copyToClipBoard
        );
    }

    public void showEditPopUp(SyncValue<Password> syncValue) {

        Password password = syncValue.getNewValue();

        final PasswordDetailsView view = PasswordDetailsView.editPassword(
                password,
                categories,
                password.getSecureDetails().getProfile(),
                this.passLength,
                onSave
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("edit.password")).show();
    }
}
