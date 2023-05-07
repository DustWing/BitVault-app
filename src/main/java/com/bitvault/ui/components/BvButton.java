package com.bitvault.ui.components;

import com.bitvault.ui.utils.JavaFxUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class BvButton extends Button {

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
        JavaFxUtil.mediumSize(this);
        return this;
    }

}
