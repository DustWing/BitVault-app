package com.bitvault.ui.components.textfield;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class LengthTextFormatter {

    public static TextFormatter<String> createMaxLength(final int maxLength) {
        UnaryOperator<TextFormatter.Change> lengthFilter = change -> {

            //If no change do nothing
            if (!change.isContentChange()) {
                return change;
            }

            String newText = change.getControlNewText();

            if (newText.length() <= maxLength) {
                return change;
            }

            String tail = newText.substring(0, maxLength);

            change.setText(tail);

            // valid coordinates for range is in terms of old text
            int oldLength = change.getControlText().length();
            change.setRange(0, oldLength);
            return change;
        };

        return new TextFormatter<>(lengthFilter);
    }


}
