package com.bitvault.components;

import com.bitvault.util.ResourceLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class BitVaultScrollPanel extends ScrollPane {

    private static final String STYLE_CLASS = "bit-vault-scroll-pane";
    private static final String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultScrollPanel.css");

    public BitVaultScrollPanel() {
        super();
        initialize();
    }

    public BitVaultScrollPanel(Node content) {
        super(content);
        initialize();
    }


    private void initialize() {
        this.setFitToWidth(true);
        this.getStyleClass().add(STYLE_CLASS);
    }


    @Override
    public String getUserAgentStylesheet() {
        return STYLE_SHEET;
    }

}
