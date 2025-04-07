package com.bankledger.validation;

import java.util.ArrayList;
import java.util.List;

public class InputValidation {

    public static List<String> validateNotBlank(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + " must not be blank.");
        }
        return errors;
    }

    public static List<String> validateDouble(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                errors.add(fieldName + " must be a valid number.");
            }
        }
        return errors;
    }
}