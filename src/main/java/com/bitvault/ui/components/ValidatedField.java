package com.bitvault.ui.components;

public interface ValidatedField {
    boolean validate();

    void clearErrors();
}
