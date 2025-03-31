package com.bankledger.model;

import java.util.Date;

public class Transaction {
    private String accountNumber;
    private double amount;
    private String type;
    private Date date;

    public Transaction(String accountNumber, double amount, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.date = new Date();
    }
}