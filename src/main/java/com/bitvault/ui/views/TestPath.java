package com.bitvault.ui.views;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class TestPath {

    public static Group test(){

        Path path = new Path();
        path.getElements().add(new MoveTo(50, 50)); // move to the starting point of the shape
        path.getElements().add(new LineTo(100, 50)); // draw a line to the next point
        path.getElements().add(new CubicCurveTo(150, 50, 150, 100, 100, 100)); // draw a curve
        path.getElements().add(new QuadCurveTo(50, 100, 50, 50)); // draw a quadratic curve
        path.getElements().add(new ClosePath()); // close the shape

        path.setStroke(Color.BLACK);
        path.setFill(Color.WHITE);

        Group root = new Group();
        root.getChildren().add(path);

        return root;
    }
}
