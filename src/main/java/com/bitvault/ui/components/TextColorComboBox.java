package com.bitvault.ui.components;

import com.bitvault.listcell.ITextColorCell;
import com.bitvault.listcell.TextColorButtonCell;
import com.bitvault.listcell.TextColorListCell;
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
