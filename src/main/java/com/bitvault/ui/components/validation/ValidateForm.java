package com.bitvault.ui.components.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public ValidateResult validate() {
        boolean valid = true;
        List<String> errorMessages = new ArrayList<>();
        for (ValidateField validatedField : validatedFieldList) {
            ValidateResult validateResult = validatedField.validate();
            if (!validateResult.valid()) {
                valid = false;
                errorMessages.addAll(validateResult.errorMessages());
            }
        }

        return new ValidateResult(valid, errorMessages);
    }


}
