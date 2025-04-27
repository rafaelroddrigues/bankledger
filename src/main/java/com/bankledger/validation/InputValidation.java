package com.bankledger.validation;

import com.bankledger.constants.Messages;

import java.util.ArrayList;
import java.util.List;

public class InputValidation {

    public static List<String> validateNotNull(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value == null) {
            errors.add(fieldName + Messages.PARAMETER_NULL);
        }
        return errors;
    }

    public static List<String> validateNotBlank(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + Messages.PARAMETER_BLANK);
        }
        return errors;
    }

    public static List<String> validateAmount(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(value);
                if (amount <= 0) {
                    errors.add(fieldName + Messages.AMOUNT_INVALID);
                }
            } catch (NumberFormatException e) {
                errors.add(fieldName + Messages.AMOUNT_INVALID);
            }
        }
        return errors;
    }

    public static List<String> validateAccountNumber(String value, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            if (!value.matches("\\d{9}")) {
                errors.add(fieldName + Messages.INVALID_ACCOUNT_NUMBER);
            }
        }
        return errors;
    }

    public static List<String> validateAccountNumberExists(boolean exists, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (exists) {
            errors.add(fieldName + Messages.ACCOUNT_NUMBER_EXISTS);
        }
        return errors;
    }

    public static List<String> validateAccountNotFound(boolean found, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (!found) {
            errors.add(fieldName + Messages.ACCOUNT_NOT_FOUND);
        }
        return errors;
    }

    public static List<String> validateSufficientBalance(boolean sufficient, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (!sufficient) {
            errors.add(Messages.INSUFFICIENT_BALANCE);
        }
        return errors;
    }
}