package com.bitvault.ui.components;

import com.bitvault.ui.utils.BvSpacing;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BvDoubleColumn extends GridPane {


    public BvDoubleColumn(List<Node> leftColumn, List<Node> rightColumn) {


        int index = 0;
        for (Node node : leftColumn) {
            this.add(node, 0, index);
            index++;
        }

        index = 0;
        for (Node node : rightColumn) {
            this.add(node, 1, index);
            index++;
        }

        // set the alignment of the first column to the right
        ColumnConstraints column1Constraints = new ColumnConstraints();
        column1Constraints.setHalignment(HPos.RIGHT);
        this.getColumnConstraints().add(column1Constraints);

        // set the alignment of the second column to the left
        ColumnConstraints column2Constraints = new ColumnConstraints();
        column2Constraints.setHalignment(HPos.LEFT);
        this.getColumnConstraints().add(column2Constraints);


        this.setAlignment(Pos.CENTER);
        this.setHgap(BvSpacing.SMALL);
    }

}


