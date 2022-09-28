package com.bitvault.util;

import javafx.scene.Scene;

public class Theme {
    public static final String DARK = "/com.bitvault/css/darkTheme.css";
    public static final String LIGHT = "/com.bitvault/css/lightTheme.css";

    public static void toDark(final Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ResourceLoader.loadURL(DARK).toExternalForm());
    }

    public static void toLight(final Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ResourceLoader.loadURL(LIGHT).toExternalForm());
    }

    public static void changeTo(final Scene scene, final String themeCss) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ResourceLoader.loadURL(themeCss).toExternalForm());
    }
}
