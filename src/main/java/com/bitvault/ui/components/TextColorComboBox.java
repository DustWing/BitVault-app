package com.bitvault.ui.components;

import com.bitvault.ui.listcell.ITextColorCell;
import com.bitvault.ui.listcell.TextColorButtonCell;
import com.bitvault.ui.listcell.TextColorListCell;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class TextColorComboBox<T extends ITextColorCell> extends ComboBox<T> {

    public TextColorComboBox() {
        super();
        init();
    }

    public TextColorComboBox(ObservableList<T> items) {
        super(items);
        init();
    }

    private void init() {
        this.setCellFactory(
                param -> new TextColorListCell<>()
        );

        this.setButtonCell(new TextColorButtonCell<>());
    }
}
