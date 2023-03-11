package com.bitvault.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Labels {

    private static final Locale grLocale = new Locale.Builder().setLanguage("gr").build();
    private static ResourceBundle resourceBundle =
            ResourceBundle.getBundle("i18n/Labels");

    public static String i18n(String key) {

        if (!resourceBundle.containsKey(key)) {
            return key;
        }
        return resourceBundle.getString(key);
    }

    public static void locale(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("i18n/Labels", locale);

    }


}
