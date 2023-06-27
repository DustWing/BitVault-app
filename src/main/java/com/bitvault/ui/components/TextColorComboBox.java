package com.bitvault.ui.components;

import atlantafx.base.theme.Styles;
import com.bitvault.ui.components.validation.ValidateField;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.listcell.ITextColorCell;
import com.bitvault.ui.listcell.TextColorListCell;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextColorComboBox<T extends ITextColorCell> extends ComboBox<T> implements ValidateField {

    boolean required = false;


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

    public TextColorComboBox<T> required(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public ValidateResult validate() {

        List<String> errorMessages = new ArrayList<>();
        boolean valid = true;
        if (required && this.getValue() == null) {
            valid = false;
            errorMessages.add("category.required");
            this.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        }

        return new ValidateResult(valid, errorMessages);

    }
}
