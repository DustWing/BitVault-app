package com.bitvault.ui.components.textfield;

import com.bitvault.ui.components.validation.ValidateField;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.utils.BvHeights;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.ui.utils.JavaFxUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import javafx.scene.control.TextFormatter;

import java.util.ArrayList;
import java.util.List;

public class BvPasswordField extends PasswordField implements ValidateField {

    boolean required = false;

    int minLength = 0;


    /**
     * Instantiates a default CustomTextField.
     */
    public BvPasswordField() {
        this.getStyleClass().add("custom-text-field");
        onWrite();
    }

    public BvPasswordField withText(String text) {
        this.setText(text);
        return this;
    }

    public BvPasswordField withPromptText(final String promptText) {
        this.setPromptText(promptText);
        return this;
    }

    public BvPasswordField withRight(final Node node) {
        this.setRight(node);
        return this;
    }

    public BvPasswordField withDefaultSize() {
        JavaFxUtil.largeSize(this);
        return this;
    }

    public BvPasswordField withBinding(final SimpleStringProperty property) {
        this.textProperty().bindBidirectional(property);
        return this;
    }

    public BvPasswordField required(boolean required) {
        this.required = required;
        this.addRequiredListener();
        return this;
    }
    public BvPasswordField maxLength(int maxLength) {
        TextFormatter<String> formatter = LengthTextFormatter.createMaxLength(maxLength);
        this.setTextFormatter(formatter);
        return this;
    }

    public BvPasswordField minLength(int minLength) {
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
            if (oldValue==null || oldValue.isBlank()) this.pseudoClassStateChanged(BvStyles.STATE_DANGER, false);
        });
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
                return BvPasswordField.this.leftProperty();
            }

            @Override
            public ObjectProperty<Node> rightProperty() {
                return BvPasswordField.this.rightProperty();
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
        } else if (getText() == null || minLength > getText().trim().length()) {
            errorMessages.add(getPromptText() + " minLength:" + minLength);
        }
        if (!valid) {
            this.pseudoClassStateChanged(BvStyles.STATE_DANGER, true);
        }

        return new ValidateResult(valid, errorMessages);
    }

}
