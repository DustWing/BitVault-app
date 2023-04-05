package com.bitvault.ui.components.validation;

import java.util.List;

public record ValidateResult(
        boolean valid,
        List<String> errorMessages
) {

}
