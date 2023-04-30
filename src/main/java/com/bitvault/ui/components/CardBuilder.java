package com.bitvault.ui.components;

import com.bitvault.ui.utils.JavaFxUtil;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CardBuilder {


    private final List<Node> nodes = new ArrayList<>();

    public CardBuilder add(Node node) {
        this.nodes.add(node);
        return this;
    }

    public CardBuilder add(List<Node> nodes) {
        this.nodes.addAll(nodes);
        return this;
    }


    public VBox build() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(nodes);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("card");
        JavaFxUtil.vGrowAlways(vBox);
        return vBox;
    }

}
