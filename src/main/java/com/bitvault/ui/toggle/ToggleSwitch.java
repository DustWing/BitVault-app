package com.bitvault.ui.toggle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;

import java.util.Objects;

/**
 * Much like a Toggle Button this control allows the user to toggle between one of two states. It has been popularized
 * in touch based devices where its usage is particularly useful because unlike a checkbox the finger touch of a user
 * doesn't obscure the control.
 *
 * <p> Shown below is a screenshot of the ToggleSwitch control in its on and off state:
 * <br>
 */
public class ToggleSwitch extends Labeled {

    /**
     * Creates a toggle switch with empty string for its label.
     */
    public ToggleSwitch() {
        initialize();
    }

    /**
     * Creates a toggle switch with the specified label.
     *
     * @param text The label string of the control.
     */
    public ToggleSwitch(String text) {
        super(text);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    /**
     * Indicates whether this ToggleSwitch is selected.
     */
    private BooleanProperty selected;

    /**
     * Sets the selected value of this Toggle Switch
     */
    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    /**
     * Returns whether this Toggle Switch is selected
     */
    public final boolean isSelected() {
        return selected != null && selected.get();
    }

    /**
     * Returns the selected property
     */
    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    final boolean v = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, v);
                }

                @Override
                public Object getBean() {
                    return ToggleSwitch.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }
            };
        }
        return selected;
    }


    /**
     * Toggles the state of the {@code ToggleSwitch}. The {@code ToggleSwitch} will cycle through
     * the selected and unselected states.
     */
    public void fire() {
        if (!isDisabled()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new ToggleSwitchSkin(this);
    }


    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "toggle-switch";

    private static final PseudoClass PSEUDO_CLASS_SELECTED =
            PseudoClass.getPseudoClass("selected");

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(ToggleSwitch.class.getResource("/com.bitvault/css/toggleSwitch.css")).toExternalForm();
    }

}