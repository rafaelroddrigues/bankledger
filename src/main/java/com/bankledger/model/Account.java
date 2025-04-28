package com.bankledger.model;

public record Account(String accountNumber, double balance) {

    public Account(String accountNumber) {
        this(accountNumber, 0);
    }

    public Account deposit(double amount) {
        return new Account(this.accountNumber, this.balance + amount);
    }

    public Account withdraw(double amount) {
        return new Account(this.accountNumber, this.balance - amount);
    }
}