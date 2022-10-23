package com.bitvault.ui.components;

import com.bitvault.util.ResourceLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BitVaultCard extends VBox {
    private final static String STYLE_CLASS = "bit-vault-card";


    public BitVaultCard(double spacing) {
        super(spacing);
        initialize();
    }

    public BitVaultCard(Node... children) {
        super(children);
        initialize();
    }

    public BitVaultCard(double spacing, Node... children) {
        super(spacing, children);
        initialize();
    }

    public BitVaultCard() {
        super();
        initialize();
    }

    private void initialize() {

        this.setFillWidth(true);
        this.setAlignment(Pos.TOP_CENTER);
        getStyleClass().add(STYLE_CLASS);
        this.getChildren().forEach(
                node -> VBox.setVgrow(node, Priority.ALWAYS)
        );
    }

    @Override
    public String getUserAgentStylesheet() {
        return ResourceLoader.load("/com.bitvault/css/bitVaultCard.css");
    }

}
