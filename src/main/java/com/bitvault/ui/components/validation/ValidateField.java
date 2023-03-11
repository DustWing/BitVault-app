package com.bitvault.ui.components.validation;

public interface ValidateField {

    boolean isRequired();
    boolean validate();
    String getError();
    void clearError();
}
