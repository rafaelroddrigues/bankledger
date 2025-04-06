package com.bankledger.repository;

import com.bankledger.model.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    public void save(Account account) {
        accounts.put(account.accountNumber(), account);
    }

    public Account findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }
}