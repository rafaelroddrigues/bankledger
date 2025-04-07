package com.bankledger.service;

import com.bankledger.exception.ExceptionList;
import com.bankledger.model.Account;
import com.bankledger.repository.AccountRepository;
import com.bankledger.validation.InputValidation;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LedgerService {

    private final AccountRepository accountRepository;

    public LedgerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(String accountNumber) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = InputValidation.validateNotBlank(accountNumber, "accountNumber");
        if (accountNumberErrors.isEmpty()) {
            accountNumberErrors.addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
            accountNumberErrors.addAll(InputValidation.validateAccountNumberExists(accountRepository.findByAccountNumber(accountNumber) != null, "accountNumber"));
        }
        errors.put("accountNumber", accountNumberErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Business logic to create account
        accountRepository.save(new Account(accountNumber));
    }

    public void deposit(String accountNumber, String amount) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = InputValidation.validateNotBlank(accountNumber, "accountNumber");
        if (accountNumberErrors.isEmpty()) {
            accountNumberErrors.addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
            Account account = accountRepository.findByAccountNumber(accountNumber);
            accountNumberErrors.addAll(InputValidation.validateAccountNotFound(account != null, "accountNumber"));
        }
        errors.put("accountNumber", accountNumberErrors);

        // Validate amount
        List<String> amountErrors = InputValidation.validateNotBlank(amount, "amount");
        if (amountErrors.isEmpty()) {
            amountErrors.addAll(InputValidation.validateAmount(amount, "amount"));
        }
        errors.put("amount", amountErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Business logic to deposit amount
        Account account = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.save(account.deposit(Double.parseDouble(amount)));
    }

    public void withdraw(String accountNumber, String amount) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = InputValidation.validateNotBlank(accountNumber, "accountNumber");
        if (accountNumberErrors.isEmpty()) {
            accountNumberErrors.addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
            Account account = accountRepository.findByAccountNumber(accountNumber);
            accountNumberErrors.addAll(InputValidation.validateAccountNotFound(account != null, "accountNumber"));
        }
        errors.put("accountNumber", accountNumberErrors);

        // Validate amount
        List<String> amountErrors = InputValidation.validateNotBlank(amount, "amount");
        if (amountErrors.isEmpty()) {
            amountErrors.addAll(InputValidation.validateAmount(amount, "amount"));
        }
        errors.put("amount", amountErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Validate sufficient balance
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            List<String> balanceErrors = InputValidation.validateSufficientBalance(account.balance() >= Double.parseDouble(amount), "amount");
            if (!balanceErrors.isEmpty()) {
                errors.put("amount", balanceErrors);
                throw new ExceptionList(errors);
            }
            accountRepository.save(account.withdraw(Double.parseDouble(amount)));
        }
    }
}