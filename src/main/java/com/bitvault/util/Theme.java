package com.bitvault.util;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.bitvault.BitVault;


public class Theme {
    public static final String DARK = "/com.bitvault/css/darkTheme.css";
    public static final String LIGHT = "/com.bitvault/css/lightTheme.css";

    public static void toDark() {
        BitVault.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

    }

    public static void toLight() {
        BitVault.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }
}
