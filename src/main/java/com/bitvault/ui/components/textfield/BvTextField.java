package com.bitvault.ui.components.textfield;

import com.bitvault.ui.components.validation.ValidateField;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BvTextField extends TextField implements ValidateField {

    boolean required = false;
    int minLength = 0;

    /**
     * Instantiates a default CustomTextField.
     */
    public BvTextField() {
        getStyleClass().add("custom-text-field");
        onWrite();
    }


    public BvTextField withText(String text) {
        this.setText(text);
        return this;
    }

    public BvTextField withPromptText(final String promptText) {
        this.setPromptText(promptText);
        return this;
    }

    public BvTextField withRight(final Node node) {
        this.setRight(node);
        return this;
    }

    public BvTextField withLeft(final Node node) {
        this.setLeft(node);
        return this;
    }

    public BvTextField withDefaultSize() {
        JavaFxUtil.defaultSize(this);
        return this;
    }

    public BvTextField withSize(double width, double height) {
        this.setMaxWidth(width);
        this.setMinWidth(width / 2);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setMinHeight(height);
        this.setMaxHeight(height);
        return this;
    }

    public BvTextField withBinding(final SimpleStringProperty property) {
        this.textProperty().bindBidirectional(property);
        return this;
    }

    public BvTextField required(boolean required) {
        this.required = required;
        this.addRequiredListener();
        return this;
    }

    public BvTextField maxLength(int maxLength) {
        TextFormatter<String> formatter = LengthTextFormatter.createMaxLength(maxLength);
        this.setTextFormatter(formatter);
        return this;
    }

    public BvTextField minLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    private void addRequiredListener() {
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {// when focus lost
                        this.pseudoClassStateChanged(BvStyles.STATE_DANGER, this.getText() != null && this.getText().isBlank());
                    }
                }
        );
    }

    private void onWrite() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            //if it was blank before remove the danger to avoid updating css
            if (oldValue == null || oldValue.isBlank()) this.pseudoClassStateChanged(BvStyles.STATE_DANGER, false);
        });
    }

    public <E> BvTextField filter(FilteredList<E> filteredList, BiFunction<E, String, Boolean> onFilter) {
        PauseTransition delay = new PauseTransition(Duration.millis(200));
        TextFieldFilterListener<E> listener = new TextFieldFilterListener<>(delay, filteredList, onFilter);
        this.textProperty().addListener(listener);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties                                                            //
    ///////////////////////////////////////////////////////////////////////////

    private final ObjectProperty<Node> left = new SimpleObjectProperty<>(this, "left");

    /**
     * Returns an ObjectProperty wrapping the {@link Node} that is placed
     * on the left of the text field.
     */
    public final ObjectProperty<Node> leftProperty() {
        return left;
    }

    /**
     * Returns the {@link Node} that is placed on the left of the text field.
     */
    public final Node getLeft() {
        return left.get();
    }

    /**
     * Sets the {@link Node} that is placed on the left of the text field.
     */
    public final void setLeft(Node value) {
        left.set(value);
    }

    private final ObjectProperty<Node> right = new SimpleObjectProperty<>(this, "right");

    /**
     * Property representing the {@link Node} that is placed on the right of the text field.
     */
    public final ObjectProperty<Node> rightProperty() {
        return right;
    }

    /**
     * Returns the {@link Node} that is placed on the right of the text field.
     */
    public final Node getRight() {
        return right.get();
    }

    /**
     * Sets the {@link Node} that is placed on the right of the text field.
     */
    public final void setRight(Node value) {
        right.set(value);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods                                                               //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new BvTextFieldSkin(this) {
            @Override
            public ObjectProperty<Node> leftProperty() {
                return BvTextField.this.leftProperty();
            }

            @Override
            public ObjectProperty<Node> rightProperty() {
                return BvTextField.this.rightProperty();
            }
        };
    }

    @Override
    public ValidateResult validate() {
        List<String> errorMessages = new ArrayList<>();
        boolean valid = true;
        if (required && (getText() == null || getText().isBlank())) {
            errorMessages.add(getPromptText() + " is required");
            valid = false;
        }

        if (getText() == null || minLength > getText().trim().length()) {
            errorMessages.add(getPromptText() + " minLength:" + minLength);
        }
        return new ValidateResult(valid, errorMessages);
    }


}
