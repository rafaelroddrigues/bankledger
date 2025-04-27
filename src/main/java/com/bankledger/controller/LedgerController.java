package com.bankledger.controller;

import com.bankledger.dto.CreateAccountRequest;
import com.bankledger.dto.DepositRequest;
import com.bankledger.dto.WithdrawRequest;
import com.bankledger.model.Account;
import com.bankledger.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private static final Logger logger = LoggerFactory.getLogger(LedgerController.class);

    @Autowired
    private LedgerService ledgerService;

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        ledgerService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        Account account = ledgerService.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        ledgerService.deposit(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        ledgerService.withdraw(request);

        return ResponseEntity.ok().build();
    }
}