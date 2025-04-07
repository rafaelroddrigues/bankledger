package com.bankledger.controller;

import com.bankledger.exception.ExceptionList;
import com.bankledger.service.LedgerService;
import com.bankledger.validation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class LedgerController {

    private static final Logger logger = LoggerFactory.getLogger(LedgerController.class);

    @Autowired
    private LedgerService ledgerService;

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestParam(name = "accountNumber", required = false) String accountNumber) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));

        try {
            ledgerService.createAccount(accountNumber);
        } catch (ExceptionList e) {
            errors.putAll(e.getErrors());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating account", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            return new ResponseEntity<>(buildErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam(name = "accountNumber", required = false) String accountNumber,
                                     @RequestParam(name = "amount", required = false) String amount) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));
        errors.put("amount", InputValidation.validateNotBlank(amount, "amount"));
        errors.get("amount").addAll(InputValidation.validateDouble(amount, "amount"));

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            return new ResponseEntity<>(buildErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        try {
            ledgerService.deposit(accountNumber, Double.parseDouble(amount));
        } catch (ExceptionList e) {
            errors.putAll(e.getErrors());
            return new ResponseEntity<>(buildErrorResponse(errors), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while depositing", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam(name = "accountNumber", required = false) String accountNumber,
                                      @RequestParam(name = "amount", required = false) String amount) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", InputValidation.validateNotBlank(accountNumber, "accountNumber"));
        errors.put("amount", InputValidation.validateNotBlank(amount, "amount"));
        errors.get("amount").addAll(InputValidation.validateDouble(amount, "amount"));

        if (errors.values().stream().anyMatch(list -> !list.isEmpty())) {
            return new ResponseEntity<>(buildErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        try {
            ledgerService.withdraw(accountNumber, Double.parseDouble(amount));
        } catch (ExceptionList e) {
            errors.putAll(e.getErrors());
            return new ResponseEntity<>(buildErrorResponse(errors), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while withdrawing", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    private Map<String, Object> buildErrorResponse(Map<String, List<String>> errors) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("errors", errors.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return errorDetails;
    }
}