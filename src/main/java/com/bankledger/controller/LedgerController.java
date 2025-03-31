package com.bankledger.controller;

import com.bankledger.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LedgerController {

    @Autowired
    private LedgerService ledgerService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the Home Page!";
    }

    @PostMapping("/account")
    public void createAccount(@RequestParam String accountNumber) {
        ledgerService.createAccount(accountNumber);
    }

    @PostMapping("/deposit")
    public void deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        ledgerService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        ledgerService.withdraw(accountNumber, amount);
    }
}