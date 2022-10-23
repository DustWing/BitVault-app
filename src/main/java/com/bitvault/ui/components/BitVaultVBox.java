package com.bitvault.ui.components;

import com.bitvault.util.ResourceLoader;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BitVaultVBox extends VBox {

    private final static String STYLE_CLASS = "bit-vault-vbox";

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
        this.getStyleClass().add(STYLE_CLASS);
    }

    protected void vGrowAlways() {
        this.getChildren().forEach(
                node -> VBox.setVgrow(node, Priority.ALWAYS)
        );

    }

    @Override
    public String getUserAgentStylesheet() {
        return ResourceLoader.load("/com.bitvault/css/bitVaultVbox.css");
    }
}
