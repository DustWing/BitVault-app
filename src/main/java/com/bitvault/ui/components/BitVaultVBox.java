package com.bitvault.ui.components;

import com.bitvault.util.ResourceLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class BitVaultVBox extends VBox {

    public BitVaultVBox() {
        super();
        initialize();
    }

    public BitVaultVBox(double spacing) {
        super(spacing);
        initialize();
    }

    public BitVaultVBox(Node... children) {
        super(children);
        initialize();
    }

    public BitVaultVBox(double spacing, Node... children) {
        super(spacing, children);
        initialize();
    }

    private void initialize() {
//        this.getStyleClass().add(STYLE_CLASS);
    }

}
