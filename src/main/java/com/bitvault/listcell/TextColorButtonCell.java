package com.bitvault.listcell;

import javafx.scene.control.ListCell;

public class TextColorButtonCell<T extends ITextColorCell> extends ListCell<T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getText());
        }
    }
}
