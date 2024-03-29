package com.bitvault.ui.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BvColors {

    private static final String template = "#%s%s%s";

    public static Color random() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    public static String randomHex() {
        return toHex(random());
    }

    public static Color fromHex(String value) {
        return Color.valueOf(value);
    }

    public static String toHex(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return String.format(
                template,
                toHex(red),
                toHex(green),
                toHex(blue)
        );
    }

    private static String toHex(int value) {
        String hexed = Integer.toHexString(value);
        return hexed.length() == 1 ? "0" + hexed : hexed;
    }

    public static final Paint LOADING_BG = Color.rgb(45, 45, 45, 0.4);

}
