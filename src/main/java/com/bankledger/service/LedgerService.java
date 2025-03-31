package com.bankledger.service;

import com.bankledger.model.Account;
import com.bankledger.model.Transaction;
import com.bankledger.repository.AccountRepository;
import com.bankledger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public LedgerService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void createAccount(String accountNumber) {
        Account account = new Account(accountNumber);
        accountRepository.save(account);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.deposit(amount);
            accountRepository.save(account);
            transactionRepository.save(new Transaction(accountNumber, amount, "deposit"));
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.withdraw(amount);
            accountRepository.save(account);
            transactionRepository.save(new Transaction(accountNumber, amount, "withdrawal"));
        }
    }
}