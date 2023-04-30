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


    final VBox childrenVBox = new VBox();

    final ScrollPane scrollPane;


    public static BvScaffold createDefault() {
        return new BvScaffold(List.of(), null);
    }

    public BvScaffold(List<Node> children, Node footer) {

        this.childrenVBox.getChildren().addAll(children);
        this.childrenVBox.setAlignment(Pos.TOP_CENTER);
        this.childrenVBox.setFillWidth(true);
        this.childrenVBox.setSpacing(BvSpacing.SMALL);
        JavaFxUtil.vGrowAlways(this.childrenVBox);

        this.scrollPane = new ScrollPane(this.childrenVBox);
        scrollPane.setFitToWidth(true);

        this.getChildren().add(scrollPane);
        if (footer != null) {
            this.getChildren().add(footer);
        }

        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        this.setSpacing(BvSpacing.SMALL);
        JavaFxUtil.vGrowAlways(this);


    }

    public void addChild(Node node) {
        this.childrenVBox.getChildren().add(node);
        JavaFxUtil.vGrowAlways(childrenVBox);
    }


    public void removeChild(Node node) {
        this.childrenVBox.getChildren().remove(node);
        this.childrenVBox.requestFocus();
    }

    public void removeChild(int index) {
        this.childrenVBox.getChildren().remove(index);
    }

    public BvScaffold withChildren(List<Node> children) {
        this.childrenVBox.getChildren().addAll(children);
        JavaFxUtil.vGrowAlways(this.childrenVBox);
        return this;
    }

    public BvScaffold withFooter(Node footer) {
        this.getChildren().add(footer);
        JavaFxUtil.vGrowAlways(this);
        return this;
    }


    public void scrollToTop(){
        scrollPane.setVvalue(0.0);
    }

    public BvScaffold enableScrollToBottom() {

        this.childrenVBox.heightProperty()
                .addListener((__, oldValue, newValue) -> {
                    if (oldValue.intValue() < newValue.intValue())
                        scrollPane.setVvalue((Double) newValue);
                });
        return this;
    }
}
