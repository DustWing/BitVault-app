package com.bitvault.ui.components;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBase;

public class BitVaultToggle extends ButtonBase {

    /** {@inheritDoc} */
    @Override
    public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }
}
