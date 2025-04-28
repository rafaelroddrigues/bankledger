package com.bankledger.unit.controller;

import com.bankledger.dto.CreateAccountRequest;
import com.bankledger.model.Account;
import com.bankledger.service.LedgerService;
import com.bankledger.controller.LedgerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class LedgerControllerTest {

    @InjectMocks
    private LedgerController ledgerController;

    @Mock
    private LedgerService ledgerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_Success() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("123456789");
        doNothing().when(ledgerService).createAccount(request);

        // Act
        ResponseEntity<?> response = ledgerController.createAccount(request);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Success_AccountNumberWithLeadingZeros() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("000123456");
        doNothing().when(ledgerService).createAccount(request);

        // Act
        ResponseEntity<?> response = ledgerController.createAccount(request);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_BlankAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("");
        doThrow(new RuntimeException("Account number cannot be blank")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Account number cannot be blank", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_AccountNumberOnlyWhitespace() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("   ");
        doThrow(new RuntimeException("Account number cannot be blank")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Account number cannot be blank", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_NullAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(null);
        doThrow(new RuntimeException("Account number must not be null")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Account number must not be null", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_AccountNumberTooShort() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("12345");
        doThrow(new RuntimeException("Invalid account number")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Invalid account number", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_AccountNumberTooLong() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("12345678901");
        doThrow(new RuntimeException("Invalid account number")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Invalid account number", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_InvalidCharacter() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("1@3a56789");
        doThrow(new RuntimeException("Invalid account number")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Invalid account number", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_AccountAlreadyExists() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("123456789");
        doThrow(new RuntimeException("Account already exists")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Account already exists", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testCreateAccount_Failure_InternalServerError() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("123456789");
        doThrow(new RuntimeException("Unexpected error")).when(ledgerService).createAccount(request);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ledgerController.createAccount(request));

        // Assert
        assertEquals("Unexpected error", exception.getMessage());
        verify(ledgerService, times(1)).createAccount(request);
    }

    @Test
    void testGetAccount_Success() {
        // Arrange
        String accountNumber = "123456789";
        Account account = new Account(accountNumber);
        when(ledgerService.getAccount(accountNumber)).thenReturn(account);

        // Act
        ResponseEntity<?> response = ledgerController.getAccount(accountNumber);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(account, response.getBody());
        verify(ledgerService, times(1)).getAccount(accountNumber);
    }

    @Test
    void testGetAccount_Failure_NotFound() {
        // Arrange
        String accountNumber = "123456789";
        when(ledgerService.getAccount(accountNumber)).thenReturn(null);

        // Act
        ResponseEntity<?> response = ledgerController.getAccount(accountNumber);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Account not found.", response.getBody());
        verify(ledgerService, times(1)).getAccount(accountNumber);
    }
}