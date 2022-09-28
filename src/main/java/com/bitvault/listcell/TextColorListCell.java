package com.bitvault.listcell;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class TextColorListCell<T extends ITextColorCell> extends ListCell<T> {

    private final Rectangle rectangle;
    private final Label label;
    private final HBox hbox;

    {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        rectangle = new Rectangle(20, 20);
        label = new Label();
        hbox = new HBox(rectangle, label);

    }


    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            label.setText(item.getText());
            rectangle.setFill(Paint.valueOf(item.getColor()));
            setGraphic(hbox);
        }
    }


}
