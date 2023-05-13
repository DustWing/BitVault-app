package com.bitvault.ui.components;

import com.bitvault.ui.components.textfield.BvPasswordField;
import com.bitvault.util.Labels;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Optional;

public class PasswordInputDialog extends Dialog<String> {

    /* ************************************************************************
     *
     * Fields
     *
     **************************************************************************/

    private final GridPane grid;
    private final Label label;
    private final TextField textField;


    /* ************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextField}.
     */
    public PasswordInputDialog() {
        this("");
    }


    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextField}.
     *
     * @param defaultValue the default value entered into the dialog
     */
    public PasswordInputDialog(@NamedArg("defaultValue") String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // -- textfield
        this.textField = new BvPasswordField().withText(defaultValue).withDefaultSize();
        this.textField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        GridPane.setFillWidth(textField, true);

        // -- label
        label = createContentLabel(dialogPane.getContentText());
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.textProperty().bind(dialogPane.contentTextProperty());

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());


        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textField.getText() : null;
        });
    }

    private static Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }



    /* ************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /**
     * Returns the {@link TextField} used within this dialog.
     *
     * @return the {@link TextField} used within this dialog
     */
    public final TextField getEditor() {
        return textField;
    }

    /**
     * Returns the default value that was specified in the constructor.
     *
     * @return the default value that was specified in the constructor
     */



    /* ************************************************************************
     *
     * Private Implementation
     *
     **************************************************************************/
    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(label, 0, 0);
        grid.add(textField, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(textField::requestFocus);
    }
}

