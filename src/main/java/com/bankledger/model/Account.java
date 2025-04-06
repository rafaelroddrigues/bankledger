package com.bankledger.model;

public record Account(String accountNumber, double amount, double balance) {

    public Account(String accountNumber) {
        this(accountNumber, 0, 0);
    }

    public Account deposit(double amount) {
        if (amount > 0) {
            return new Account(this.accountNumber, this.amount, this.balance + amount);
        }
        return this;
    }

    public Account withdraw(double amount) {
        return new Account(this.accountNumber, amount, this.balance);
    }
}