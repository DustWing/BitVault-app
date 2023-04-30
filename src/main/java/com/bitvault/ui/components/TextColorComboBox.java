package com.bitvault.ui.components;

import com.bitvault.ui.listcell.ITextColorCell;
import com.bitvault.ui.listcell.TextColorListCell;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.function.Supplier;

public class TextColorComboBox<T extends ITextColorCell> extends ComboBox<T> {

    public static <T extends ITextColorCell> TextColorComboBox<T> withRectangle(ObservableList<T> items) {

        final Supplier<Shape> shape = () -> new Rectangle(20, 20);

        return new TextColorComboBox<>(items, shape);
    }

    public static <T extends ITextColorCell> TextColorComboBox<T> withCircle(ObservableList<T> items) {

        final Supplier<Shape> shape = () -> new Circle(5);

        return new TextColorComboBox<>(items, shape);
    }

    public TextColorComboBox(ObservableList<T> items, Supplier<Shape> shape) {
        super(items);
        this.setCellFactory(param -> new TextColorListCell<>(shape.get()));
        this.setButtonCell(new TextColorListCell<>(shape.get()));
    }


}
