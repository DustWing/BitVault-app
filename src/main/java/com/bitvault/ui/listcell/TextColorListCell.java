package com.bitvault.ui.listcell;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class TextColorListCell<T extends ITextColorCell> extends ListCell<T> {

    private final Shape shape;
    private final Label label;
    private final HBox hbox;


    public TextColorListCell(Shape shape) {

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.shape = shape;
        this.label = new Label();
        this.hbox = new HBox(shape, label);

    }


    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {


            label.setText(item.getText());
            shape.setFill(Paint.valueOf(item.getColor()));
            setGraphic(hbox);
        }
    }


}
