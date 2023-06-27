package com.bitvault.ui.views.sync;

import atlantafx.base.theme.Styles;
import com.bitvault.ui.hyperlink.IWebLocation;
import com.bitvault.ui.model.SyncValue;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignA.ALERT_BOX_OUTLINE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CHECK_BOX_OUTLINE;

public class SyncTableIconCellFactory<T extends IWebLocation> extends TableCell<SyncValue<T>, SyncValue.ActionState> {

    @Override
    protected void updateItem(SyncValue.ActionState item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || getTableRow() == null) {
            setGraphic(null);
            return;
        }

        setGraphic(getIcon(item));

    }


    private Node getIcon(SyncValue.ActionState actionState) {

        if (actionState.equals(SyncValue.ActionState.REQUIRED)) {
            FontIcon warning = new FontIcon(ALERT_BOX_OUTLINE);
            warning.pseudoClassStateChanged(Styles.STATE_WARNING, true);
            return warning;
        }

        FontIcon checkBox = new FontIcon(CHECK_BOX_OUTLINE);
        checkBox.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);

        return checkBox;

    }
}
