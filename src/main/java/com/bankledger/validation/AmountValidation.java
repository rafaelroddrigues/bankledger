package com.bankledger.validation;

import java.util.ArrayList;
import java.util.List;

public class AmountValidation {

    public static List<String> validateAmount(String amount, String fieldName) {
        List<String> errors = new ArrayList<>(CommonValidation.validateNotNullAndNotBlank(amount, fieldName));
        if (errors.isEmpty()) {
            errors.addAll(InputValidation.validateAmount(amount, "amount"));
        }
        return errors;
    }
}