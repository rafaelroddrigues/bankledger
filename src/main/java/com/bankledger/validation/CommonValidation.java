package com.bankledger.validation;

import java.util.ArrayList;
import java.util.List;

public class CommonValidation {

    public static List<String> validateNotNullAndNotBlank(String value, String fieldName) {
        List<String> errors = new ArrayList<>(InputValidation.validateNotNull(value, fieldName));
        if (errors.isEmpty()) {
            errors.addAll(InputValidation.validateNotBlank(value, fieldName));
        }
        return errors;
    }
}