package com.bankledger.service;

import com.bankledger.dto.CreateAccountRequest;
import com.bankledger.dto.DepositRequest;
import com.bankledger.dto.WithdrawRequest;
import com.bankledger.exception.ExceptionList;
import com.bankledger.model.Account;
import com.bankledger.repository.AccountRepository;
import com.bankledger.validation.AccountValidation;
import com.bankledger.validation.AmountValidation;
import com.bankledger.validation.BalanceValidation;
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

    public void createAccount(CreateAccountRequest request) throws ExceptionList {
        String accountNumber = request.accountNumber();
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = AccountValidation.validateNewAccountNumber(accountNumber, accountRepository, "accountNumber");
        errors.put("accountNumber", accountNumberErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Create account
        accountRepository.save(new Account(accountNumber));
    }

    public Account getAccount(String accountNumber) throws ExceptionList {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = AccountValidation.validateExistingAccountNumber(accountNumber, accountRepository, "accountNumber");
        errors.put("accountNumber", accountNumberErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Get account
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void deposit(DepositRequest request) throws ExceptionList {
        String accountNumber = request.accountNumber();
        String amount = request.amount();
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = AccountValidation.validateExistingAccountNumber(accountNumber, accountRepository, "accountNumber");
        errors.put("accountNumber", accountNumberErrors);

        // Validate amount
        List<String> amountErrors = AmountValidation.validateAmount(amount, "amount");
        errors.put("amount", amountErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Deposit amount
        Account account = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.save(account.deposit(Double.parseDouble(amount)));
    }

    public void withdraw(WithdrawRequest request) throws ExceptionList {
        String accountNumber = request.accountNumber();
        String amount = request.amount();
        Map<String, List<String>> errors = new LinkedHashMap<>();

        // Validate account number
        List<String> accountNumberErrors = AccountValidation.validateExistingAccountNumber(accountNumber, accountRepository, "accountNumber");
        errors.put("accountNumber", accountNumberErrors);

        // Validate amount
        List<String> amountErrors = AmountValidation.validateAmount(amount, "amount");
        errors.put("amount", amountErrors);

        // Validate sufficient balance
        List<String> balanceErrors = BalanceValidation.validateSufficientBalance(accountNumber, accountRepository, amount, "amount");
        errors.put("amount", balanceErrors);

        // Check for any errors before proceeding
        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            throw new ExceptionList(errors);
        }

        // Withdraw amount
        Account account = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.save(account.withdraw(Double.parseDouble(amount)));
    }
}