package com.bitvault.ui.components.grid;

import com.bitvault.ui.utils.BvSpacing;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BvSimpleGrid extends GridPane {

    public static BvSimpleGrid createSingleDoubleColumn(Node left, Node right) {

        final GridRow gridRow = new GridRow(List.of(left, right));

        return createDoubleColumn(List.of(gridRow));
    }

    public static BvSimpleGrid createDoubleColumn(List<GridRow> rows) {

        final BvSimpleGrid bvSimpleGrid = new BvSimpleGrid(rows);

        // set the alignment of the first column to the right
        final ColumnConstraints column1Constraints = new ColumnConstraints();
        column1Constraints.setHalignment(HPos.RIGHT);
        bvSimpleGrid.getColumnConstraints().add(column1Constraints);

        // set the alignment of the second column to the left
        final ColumnConstraints column2Constraints = new ColumnConstraints();
        column2Constraints.setHalignment(HPos.LEFT);
        bvSimpleGrid.getColumnConstraints().add(column2Constraints);

        return bvSimpleGrid;
    }

    public BvSimpleGrid(List<GridRow> rows) {
        int rowIndex = 0;

        for (GridRow row : rows) {
            int columnIndex = 0;
            for (Node node : row.nodes()) {
                this.add(node, columnIndex, rowIndex);
                columnIndex++;
            }
            rowIndex++;
        }


        this.setAlignment(Pos.CENTER);
        this.setHgap(BvSpacing.SMALL);
        this.setVgap(BvSpacing.SMALL);
    }

}


