package com.bankledger.validation;

import com.bankledger.model.Account;
import com.bankledger.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;

public class AccountValidation {

    public static List<String> validateNewAccountNumber(String accountNumber, AccountRepository accountRepository, String fieldName) {
        List<String> errors = new ArrayList<>(CommonValidation.validateNotNullAndNotBlank(accountNumber, fieldName));
        if (errors.isEmpty()) {
            errors.addAll(InputValidation.validateAccountNumber(accountNumber, fieldName));
            errors.addAll(InputValidation.validateAccountNumberExists(accountRepository.findByAccountNumber(accountNumber) != null, fieldName));
        }
        return errors;
    }

    public static List<String> validateExistingAccountNumber(String accountNumber, AccountRepository accountRepository, String fieldName) {
        List<String> errors = new ArrayList<>(CommonValidation.validateNotNullAndNotBlank(accountNumber, fieldName));
        if (errors.isEmpty()) {
            Account account = accountRepository.findByAccountNumber(accountNumber);
            errors.addAll(InputValidation.validateAccountNotFound(account != null, "accountNumber"));
        }
        return errors;
    }
}