package com.bitvault.ui.utils;

import com.bitvault.util.ResourceLoader;
import javafx.scene.image.Image;

import java.net.URL;


public class IconUtils {
    public static final URL ICON_URL = ResourceLoader.loadURL("/com.bitvault/icons/32moth.png");

    public static final Image STAGE_ICON = new Image(ICON_URL.toExternalForm());


}
