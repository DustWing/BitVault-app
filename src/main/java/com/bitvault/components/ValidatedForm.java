package com.bitvault.components;

import java.util.ArrayList;
import java.util.List;

public class ValidatedForm {

    private final List<ValidatedField> validatedFieldList;

    public ValidatedForm() {
        validatedFieldList = new ArrayList<>();
    }

    public ValidatedForm(List<ValidatedField> validatedFieldList) {
        this.validatedFieldList = validatedFieldList;
    }

    public void add(ValidatedField field) {
        validatedFieldList.add(field);
    }

    public void addAll(ValidatedField... fields) {
        validatedFieldList.addAll(List.of(fields));
    }


    public boolean validate() {

        boolean valid = true;
        for (ValidatedField validatedField : validatedFieldList) {
            if (!validatedField.validate()) {
                valid = false;
            }
        }
        return valid;
    }

    public void clear() {
        validatedFieldList.forEach(ValidatedField::clearErrors);
    }
}
