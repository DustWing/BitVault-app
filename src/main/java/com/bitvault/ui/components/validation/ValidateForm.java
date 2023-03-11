package com.bitvault.ui.components.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidateForm {

    private final List<ValidateField> validatedFieldList;

    public ValidateForm() {
        validatedFieldList = new ArrayList<>();
    }

    public ValidateForm(List<ValidateField> validatedFieldList) {
        this.validatedFieldList = validatedFieldList;
    }

    public void add(ValidateField field) {
        validatedFieldList.add(field);
    }

    public void addAll(ValidateField... fields) {
        validatedFieldList.addAll(List.of(fields));
    }


    public boolean validate() {

        boolean valid = true;
        for (ValidateField validatedField : validatedFieldList) {
            if (!validatedField.validate()) {
                valid = false;
            }
        }
        return valid;
    }

    public void clear() {
        validatedFieldList.forEach(ValidateField::clearError);
    }
}
