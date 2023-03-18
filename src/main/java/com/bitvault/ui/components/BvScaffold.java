package com.bitvault.ui.components;

import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.BvSpacing;
import com.bitvault.ui.utils.JavaFxUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class BvScaffold extends VBox {


    public BvScaffold(List<Node> children, Node footer) {

        final VBox childrenVBox = new VBox();
        childrenVBox.getChildren().addAll(children);
        childrenVBox.setAlignment(Pos.TOP_CENTER);
        childrenVBox.setFillWidth(true);
        childrenVBox.setSpacing(BvSpacing.SMALL);


        final ScrollPane scrollPane = new ScrollPane(childrenVBox);
        scrollPane.setFitToWidth(true);


        this.getChildren().addAll(
                scrollPane,
                footer
        );

        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        JavaFxUtil.vGrowAlways(this);

    }

}
