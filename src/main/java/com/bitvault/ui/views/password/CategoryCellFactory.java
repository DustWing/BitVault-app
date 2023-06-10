package com.bitvault.ui.views.password;

import com.bitvault.ui.model.Category;
import javafx.scene.control.TableCell;

public class CategoryCellFactory<T> extends TableCell<T, Category> {

    @Override
    protected void updateItem(Category item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.name());
        }
    }

}
