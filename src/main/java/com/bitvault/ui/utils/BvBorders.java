package com.bitvault.ui.utils;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BvBorders {
    public static Border THICK_DEFAULT() {
        return new Border(
                new BorderStroke(
                        Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THICK
                )
        );
    }

    public static Border THICK_DEFAULT(Color color) {
        return new Border(
                new BorderStroke(
                        color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THICK
                )
        );
    }
}
