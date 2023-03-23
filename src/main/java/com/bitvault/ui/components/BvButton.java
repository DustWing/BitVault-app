package com.bitvault.ui.components;

import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class BvButton extends Button {

    private final static String STYLE_CLASS = "bit-vault-flat-button";
    private final static String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultButton.css");

    public BvButton() {
        super();
    }

    public BvButton(String text) {
        super(text);
    }

    public BvButton(String text, Node graphic) {
        super(text, graphic);
    }


    public BvButton action(EventHandler<ActionEvent> value){
        this.setOnAction(value);
        return this;
    }

    public BvButton defaultButton(boolean value){
        this.setDefaultButton(value);
        return this;
    }
    public BvButton withDefaultSize() {
        JavaFxUtil.defaultSize(this);
        return this;
    }

//    @Override
//    public String getUserAgentStylesheet() {
//        return STYLE_SHEET;
//    }
}
