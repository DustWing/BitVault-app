package com.bitvault.ui.components.textfield;

import com.bitvault.ui.utils.BvHeights;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.ui.utils.JavaFxUtil;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.function.BiFunction;

public class BvTextField extends TextField {

//    private final static String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultTextField.css");
//    @Override
//    public String getUserAgentStylesheet() {
//        return STYLE_SHEET;
//    }

    boolean required = false;

    /**
     * Instantiates a default CustomTextField.
     */
    public BvTextField() {
        getStyleClass().add("custom-text-field");
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

    public BvTextField withDefaultSize() {
        JavaFxUtil.defaultSize(this);
        return this;
    }

    public BvTextField withBinding(final SimpleStringProperty property) {
        this.textProperty().bindBidirectional(property);
        return this;
    }

    public BvTextField setRequired(boolean required) {
        this.required = required;
        return this;
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
}
