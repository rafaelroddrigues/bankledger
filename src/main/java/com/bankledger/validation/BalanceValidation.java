package com.bankledger.validation;

import com.bankledger.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;

public class BalanceValidation {

    public static List<String> validateSufficientBalance(String accountNumber, AccountRepository accountRepository, String amount, String fieldName) {

        // Validate amount using InputValidation
        List<String> errors = new ArrayList<>(AmountValidation.validateAmount(amount, fieldName));
        if (!errors.isEmpty()) {
            return errors;
        }

        // Retrieve the account and check balance
        var account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            errors.addAll(InputValidation.validateSufficientBalance(account.balance() >= Double.parseDouble(amount), "amount"));
        }

        return errors;
    }
}