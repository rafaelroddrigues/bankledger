package com.bankledger.service;

import com.bankledger.exception.ExceptionList;
import com.bankledger.model.Account;
import com.bankledger.model.Transaction;
import com.bankledger.repository.AccountRepository;
import com.bankledger.repository.TransactionRepository;
import com.bankledger.constants.Messages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public LedgerService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void createAccount(String accountNumber) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> accountNumberErrors = new ArrayList<>();
        if (!accountNumber.matches("\\d{9}")) {
            accountNumberErrors.add(Messages.INVALID_ACCOUNT_NUMBER);
        }
        if (accountRepository.findByAccountNumber(accountNumber) != null) {
            accountNumberErrors.add(Messages.ACCOUNT_NUMBER_EXISTS);
        }
        if (!accountNumberErrors.isEmpty()) {
            errors.put("accountNumber", accountNumberErrors);
        }
        if (!errors.isEmpty()) {
            throw new ExceptionList(errors);
        }
        Account account = new Account(accountNumber);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(String accountNumber, double amount) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> accountNumberErrors = new ArrayList<>();
        List<String> amountErrors = new ArrayList<>();
        if (!accountNumber.matches("\\d{9}")) {
            accountNumberErrors.add(Messages.INVALID_ACCOUNT_NUMBER);
        }
        if (amount <= 0) {
            amountErrors.add(Messages.DEPOSIT_AMOUNT_INVALID);
        }
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            accountNumberErrors.add(Messages.ACCOUNT_NOT_FOUND);
        }
        if (!accountNumberErrors.isEmpty()) {
            errors.put("accountNumber", accountNumberErrors);
        }
        if (!amountErrors.isEmpty()) {
            errors.put("amount", amountErrors);
        }
        if (!errors.isEmpty()) {
            throw new ExceptionList(errors);
        }
        account.deposit(amount);
        accountRepository.save(account);
        transactionRepository.save(new Transaction(accountNumber, amount, "deposit"));
    }

    @Transactional
    public void withdraw(String accountNumber, double amount) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> accountNumberErrors = new ArrayList<>();
        List<String> amountErrors = new ArrayList<>();
        if (!accountNumber.matches("\\d{9}")) {
            accountNumberErrors.add(Messages.INVALID_ACCOUNT_NUMBER);
        }
        if (amount <= 0) {
            amountErrors.add(Messages.WITHDRAWAL_AMOUNT_INVALID);
        }
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            accountNumberErrors.add(Messages.ACCOUNT_NOT_FOUND);
        } else if (account.balance() < amount) {
            amountErrors.add(Messages.INSUFFICIENT_BALANCE);
        }
        if (!accountNumberErrors.isEmpty()) {
            errors.put("accountNumber", accountNumberErrors);
        }
        if (!amountErrors.isEmpty()) {
            errors.put("amount", amountErrors);
        }
        if (!errors.isEmpty()) {
            throw new ExceptionList(errors);
        }
        account.withdraw(amount);
        accountRepository.save(account);
        transactionRepository.save(new Transaction(accountNumber, amount, "withdrawal"));
    }
}