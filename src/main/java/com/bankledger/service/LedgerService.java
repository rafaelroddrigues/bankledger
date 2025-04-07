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
        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));
        errors.get("accountNumber").addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
        errors.get("accountNumber").addAll(InputValidation.validateAccountNumberExists(accountRepository.findByAccountNumber(accountNumber) != null, "accountNumber"));

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Business logic to create account
        accountRepository.save(new Account(accountNumber));
    }

    public void deposit(String accountNumber, String amount) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));
        errors.get("accountNumber").addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
        Account account = accountRepository.findByAccountNumber(accountNumber);
        errors.get("accountNumber").addAll(InputValidation.validateAccountNotFound(account != null, "accountNumber"));
        errors.put("amount", InputValidation.validateNotBlank(amount, "amount"));
        errors.get("amount").addAll(InputValidation.validateAmount(amount, "amount"));

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Business logic to deposit amount
        accountRepository.save(account.deposit(Double.parseDouble(amount)));
    }

    public void withdraw(String accountNumber, String amount) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));
        errors.get("accountNumber").addAll(InputValidation.validateAccountNumber(accountNumber, "accountNumber"));
        Account account = accountRepository.findByAccountNumber(accountNumber);
        errors.get("accountNumber").addAll(InputValidation.validateAccountNotFound(account != null, "accountNumber"));
        errors.put("amount", InputValidation.validateNotBlank(amount, "amount"));
        errors.get("amount").addAll(InputValidation.validateAmount(amount, "amount"));

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        if (account != null) {
            errors.get("amount").addAll(InputValidation.validateSufficientBalance(account.balance() >= Double.parseDouble(amount), "amount"));
        }

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Business logic to withdraw amount
        accountRepository.save(account.withdraw(Double.parseDouble(amount)));
    }
}