package com.bitvault.ui.components;

import com.bitvault.ui.utils.BvInsets;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class Section {

    public static StackPane create(String labelTxt, List<Node> nodes) {
        VBox vBox = new CardBuilder().add(nodes).build();
        Label label = new Label(labelTxt);
        label.setStyle(" -fx-background-color: #0d1117;");
        label.setPadding(new Insets(0, 5, 0, 5));

        StackPane.setMargin(label, new Insets(-10, 0, 0, 10));
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        StackPane stackPane = new StackPane(vBox, label);
        stackPane.setPadding(BvInsets.top10);

        label.setLabelFor(vBox);
        return stackPane;
    }

}
