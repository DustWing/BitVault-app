package com.bitvault.ui.views.sync;

import com.bitvault.ui.hyperlink.IWebLocation;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.ui.utils.BvStyles;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignA.ALERT_BOX_OUTLINE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CHECK_BOX_OUTLINE;

public class SyncTableIconCellFactory<T extends IWebLocation> extends TableCell<SyncValue<T>, Boolean> {

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || getTableRow() == null) {
            setGraphic(null);
            return;
        }

        setGraphic(getIcon(item));

    }


    public Node getIcon(boolean isWarning) {

        if (isWarning) {
            FontIcon warning = new FontIcon(ALERT_BOX_OUTLINE);
            warning.pseudoClassStateChanged(BvStyles.STATE_WARNING, true);
            return warning;
        }

        FontIcon checkBox = new FontIcon(CHECK_BOX_OUTLINE);
        checkBox.pseudoClassStateChanged(BvStyles.STATE_SUCCESS, true);

        return checkBox;

    }
}