package com.bitvault.ui.utils;

import javafx.geometry.Insets;

public class BvInsets {
    public static Insets right(double value) {
        return new Insets(0, value, 0, 0);
    }

    public static Insets left(double value) {
        return new Insets(0, 0, 0, value);
    }

    public static Insets top(double value) {
        return new Insets(value, 0, 0, 0);
    }

    public static Insets bottom(double value) {
        return new Insets(0, 0, value, 0);
    }

    public static Insets right5 = right(5);
    public static Insets right10 = right(10);
    public static Insets right15 = right(15);

    public static Insets left5 = left(5);
    public static Insets left10 = left(10);
    public static Insets left15 = left(15);

    public static Insets top5 = top(5);
    public static Insets top10 = top(10);
    public static Insets top15 = top(15);

    public static Insets bottom5 = bottom(5);
    public static Insets bottom10 = bottom(10);
    public static Insets bottom15 = bottom(15);

    public static Insets all10 = new Insets(10);
    public static Insets all20 = new Insets(20);


}
