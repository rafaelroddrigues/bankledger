package com.bankledger.model;

import lombok.Data;

@Data
public class Account {

    private final String accountNumber;
    private double amount;
    private double balance;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void withdraw(double amount) {
        this.amount = amount;
    }
}