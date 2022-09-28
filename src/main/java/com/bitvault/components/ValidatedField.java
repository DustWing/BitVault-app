package com.bitvault.components;

public interface ValidatedField {
    boolean validate();

    void clearErrors();
}
