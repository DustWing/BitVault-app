package com.bitvault.ui.views.sync;

import com.bitvault.ui.hyperlink.IWebLocation;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.util.Labels;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;

public class SyncTableRowFactory<T extends IWebLocation> extends TableRow<SyncValue<T>> {


    public SyncTableRowFactory() {
        super();
    }

    @Override
    protected void updateItem(SyncValue<T> item, boolean empty) {
        super.updateItem(item, empty);

        //do nothing
        if (item == null || empty) {
            return;
        }


//        pseudoClassStateChanged(BvStyles.STATE_WARNING, item.warning());

        addContextMenu(this);

    }


    private void addContextMenu(TableRow<SyncValue<T>> row) {


        final ContextMenu contextMenu = new ContextMenu();
        SyncValue<T> selectedItem = row.getItem();

        MenuItem details = new MenuItem(Labels.i18n("details"));
        details.setOnAction(event -> {
                    System.out.println(selectedItem);
                }
        );
        contextMenu.getItems().add(details);


        if (selectedItem.warning() || SyncValue.Action.DISCARD.equals(selectedItem.getAction())) {
            MenuItem acceptNew = new MenuItem(Labels.i18n("accept.new"));
            acceptNew.setOnAction(
                    event -> {
                        selectedItem.setAction(SyncValue.Action.SAVE);
                        getTableView().refresh();
                    }
            );
            contextMenu.getItems().add(acceptNew);
        }

        if (selectedItem.warning() || SyncValue.Action.SAVE.equals(selectedItem.getAction())) {
            MenuItem discard = new MenuItem(Labels.i18n("discard"));
            discard.setOnAction(
                    event -> {
                        selectedItem.setAction(SyncValue.Action.DISCARD);
                        getTableView().refresh();
                    }
            );
            contextMenu.getItems().add(discard);
        }

        row.setContextMenu(contextMenu);
    }


}
