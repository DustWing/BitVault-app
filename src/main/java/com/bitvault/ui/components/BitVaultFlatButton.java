package com.bitvault.ui.components;

import com.bitvault.ui.utils.BvHeights;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.util.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class BitVaultFlatButton extends Button {

    private final static String STYLE_CLASS = "bit-vault-flat-button";
    private final static String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultButton.css");

    public BitVaultFlatButton() {
        super();
        initialize();
    }

    public BitVaultFlatButton(String text) {
        super(text);
        initialize();
    }

    public BitVaultFlatButton(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    public BitVaultFlatButton action(EventHandler<ActionEvent> value){
        this.setOnAction(value);
        return this;
    }

    private void initialize() {
        getStyleClass().add(STYLE_CLASS);
        this.setPrefWidth(BvWidths.LARGE);
        this.setMaxWidth(BvWidths.LARGE);
        this.setMinHeight(BvHeights.MEDIUM);
        this.setMaxHeight(BvHeights.MEDIUM);
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLE_SHEET;
    }
}
