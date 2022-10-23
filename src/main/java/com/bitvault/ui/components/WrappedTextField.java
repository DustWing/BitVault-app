package com.bitvault.ui.components;

import com.bitvault.ui.utils.BvHeights;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.util.ResourceLoader;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.function.BiFunction;

public class WrappedTextField extends BitVaultVBox implements ValidatedField {

    private final static String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultTextField.css");
    private final TextField textField;
    private final Label errorLbl;
    private boolean required = false;

    public WrappedTextField() {
        this.textField = new TextField();
        this.errorLbl = new Label();
        init();
    }

    private void init() {

        this.textField.getStyleClass().add("bit-vault-text-field");
        this.textField.setMaxWidth(BvWidths.LARGE);
        this.textField.setPrefWidth(BvWidths.LARGE);
        this.textField.setPrefHeight(BvHeights.MEDIUM);

        this.errorLbl.getStyleClass().add("bit-vault-text-field-error");
//        this.label.setLabelFor(this.textField);

        this.getChildren().addAll(
                this.textField,
                this.errorLbl
        );

        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        super.vGrowAlways();
    }

    public void error(final String errorMsg) {
        errorLbl.setText(errorMsg);
    }

    public WrappedTextField text(final String text) {
        textField.setText(text);
        return this;
    }

    public WrappedTextField required(final boolean required) {
        this.required = required;
        return this;
    }

    public WrappedTextField withPlaceholder(final String placeholder) {
        textField.setPromptText(placeholder);
        return this;
    }

    public WrappedTextField withBinding(final SimpleStringProperty property) {
        textField.textProperty().bindBidirectional(property);
        return this;
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLE_SHEET;
    }


    public <E> WrappedTextField withFilter(FilteredList<E> filteredList, BiFunction<E, String, Boolean> onFilter) {
        final PauseTransition pause = new PauseTransition(Duration.millis(200));
        this.textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    pause.setOnFinished(
                            event -> {
                                if (newValue == null || newValue.isBlank()) {
                                    filteredList.setPredicate(s -> true);
                                } else {
                                    filteredList.setPredicate(
                                            e -> onFilter.apply(e, newValue)
                                    );
                                }
                            }
                    );
                    pause.playFromStart();

                }
        );
        return this;
    }


    public String getText() {
        return textField.getText();
    }

    @Override
    public boolean validate() {
        if (required
                && (textField.getText() == null || textField.getText().isBlank())
        ) {
            error("Required Field");
            return false;
        }
        return true;
    }

    @Override
    public void clearErrors() {
        error("");
    }
}
