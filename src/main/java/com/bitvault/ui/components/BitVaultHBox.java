package com.bitvault.ui.components;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class BitVaultHBox extends HBox {
    private final static String STYLE_CLASS = "bit-vault-hbox";

    public BitVaultHBox() {
        super();
        initialize();
    }

    public BitVaultHBox(double spacing) {
        super(spacing);
        initialize();
    }

    public BitVaultHBox(Node... children) {
        super(children);
        initialize();
    }

    public BitVaultHBox(double spacing, Node... children) {
        super(spacing, children);
        initialize();
    }

    private void initialize() {
        this.getStyleClass().add(STYLE_CLASS);

        this.getChildren().forEach(
                node -> HBox.setHgrow(node, Priority.ALWAYS)
        );

    }
}
