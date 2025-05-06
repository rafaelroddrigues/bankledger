package com.bankledger.unit.service;

import com.bankledger.dto.CreateAccountRequest;
import com.bankledger.dto.DepositRequest;
import com.bankledger.dto.WithdrawRequest;
import com.bankledger.exception.ExceptionList;
import com.bankledger.model.Account;
import com.bankledger.repository.AccountRepository;
import com.bankledger.service.LedgerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.bankledger.constants.Messages;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class LedgerServiceTest {

    @InjectMocks
    private LedgerService ledgerService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_Success() throws ExceptionList {
        // Arrange
        String accountNumber = "123456789";
        CreateAccountRequest request = new CreateAccountRequest(accountNumber);

        // Act
        ledgerService.createAccount(request);

        // Assert
        verify(accountRepository, times(1)).save(argThat(account -> account.accountNumber().equals(accountNumber)));
    }

    @Test
    void testCreateAccount_Failure_ShortAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("12345678"); // Invalid account number
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", List.of(Messages.INVALID_ACCOUNT_NUMBER));
        doThrow(new ExceptionList(errors)).when(accountRepository).save(any(Account.class));

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.createAccount(request));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.INVALID_ACCOUNT_NUMBER));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Failure_LargeAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("1234567891"); // Invalid account number
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", List.of(Messages.INVALID_ACCOUNT_NUMBER));
        doThrow(new ExceptionList(errors)).when(accountRepository).save(any(Account.class));

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.createAccount(request));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.INVALID_ACCOUNT_NUMBER));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Failure_LetterAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest("a2b4c6d8e"); // Invalid account number
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", List.of(Messages.INVALID_ACCOUNT_NUMBER));
        doThrow(new ExceptionList(errors)).when(accountRepository).save(any(Account.class));

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.createAccount(request));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.INVALID_ACCOUNT_NUMBER));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Failure_BlankAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(""); // Blank account number
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", List.of(Messages.PARAMETER_BLANK));
        doThrow(new ExceptionList(errors)).when(accountRepository).save(any(Account.class));

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.createAccount(request));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.PARAMETER_BLANK));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Failure_NullAccountNumber() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(null); // Null account number
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("accountNumber", List.of(Messages.PARAMETER_NULL));
        doThrow(new ExceptionList(errors)).when(accountRepository).save(any(Account.class));

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.createAccount(request));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.PARAMETER_NULL));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testGetAccount_Success() throws ExceptionList {
        // Arrange
        String accountNumber = "123456789";
        Account expectedAccount = new Account(accountNumber);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(expectedAccount);

        // Act
        Account actualAccount = ledgerService.getAccount(accountNumber);

        // Assert
        assertNotNull(actualAccount);
        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository, times(2)).findByAccountNumber(accountNumber);
    }

    @Test
    void testGetAccount_Failure_AccountDoesNotExist() {
        // Arrange
        String accountNumber = "123456789";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.getAccount(accountNumber));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.ACCOUNT_NOT_FOUND));
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void testGetAccount_Failure_NullAccountNumber() {
        // Arrange
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(null);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.getAccount(null));

        // Assert
        assertTrue(exception.getErrors().get("accountNumber").toString().contains(Messages.PARAMETER_NULL));
        verify(accountRepository, never()).findByAccountNumber(anyString());
    }

    @Test
    void testDeposit_Success() throws ExceptionList {
        // Arrange
        String accountNumber = "123456789";
        String depositAmount = "100.00";
        Account account = new Account(accountNumber, 0.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        // Act
        ledgerService.deposit(new DepositRequest(accountNumber, depositAmount));

        // Assert
        verify(accountRepository, times(1)).save(argThat(updatedAccount ->
                updatedAccount.accountNumber().equals(accountNumber) &&
                        updatedAccount.balance() == 100.0
        ));
    }

    @Test
    void testDeposit_Failure_NegativeAmount() {
        // Arrange
        String accountNumber = "123456789";
        String invalidAmount = "-100.00"; // Negative amount
        DepositRequest request = new DepositRequest(accountNumber, invalidAmount);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.deposit(request));

        // Assert
        assertTrue(exception.getErrors().containsKey("amount"));
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.AMOUNT_INVALID));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testDeposit_Failure_BlankAmount() {
        // Arrange
        String accountNumber = "123456789";
        String blankAmount = ""; // Blank amount
        DepositRequest request = new DepositRequest(accountNumber, blankAmount);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.deposit(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.PARAMETER_BLANK));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testDeposit_Failure_NullAmount() {
        // Arrange
        String accountNumber = "123456789";
        DepositRequest request = new DepositRequest(accountNumber, null);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.deposit(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.PARAMETER_NULL));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testDeposit_Failure_LetterAmount() {
        // Arrange
        String accountNumber = "123456789";
        String letterAmount = "abc"; // Non-numeric amount
        DepositRequest request = new DepositRequest(accountNumber, letterAmount);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.deposit(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.AMOUNT_INVALID));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Success() throws ExceptionList {
        // Arrange
        String accountNumber = "123456789";
        String withdrawAmount = "50.00";
        Account account = new Account(accountNumber, 100.0); // Initial balance
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        // Act
        ledgerService.withdraw(new WithdrawRequest(accountNumber, withdrawAmount));

        // Assert
        verify(accountRepository, times(1)).save(argThat(updatedAccount ->
                updatedAccount.accountNumber().equals(accountNumber) &&
                        updatedAccount.balance() == 50.0 // Remaining balance
        ));
    }

    @Test
    void testWithdraw_Failure_InvalidAmount() {
        // Arrange
        String accountNumber = "123456789";
        String invalidAmount = "-50.00"; // Negative amount
        WithdrawRequest request = new WithdrawRequest(accountNumber, invalidAmount);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.withdraw(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.AMOUNT_INVALID));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Failure_BlankAmount() {
        // Arrange
        String accountNumber = "123456789";
        String blankAmount = ""; // Blank amount
        WithdrawRequest request = new WithdrawRequest(accountNumber, blankAmount);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.withdraw(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.PARAMETER_BLANK));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Failure_NullAmount() {
        // Arrange
        String accountNumber = "123456789";
        WithdrawRequest request = new WithdrawRequest(accountNumber, null);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.withdraw(request));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.PARAMETER_NULL));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Failure_AccountWithZeroBalance() {
        // Arrange
        String accountNumber = "123456789";
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal amountToWithdraw = new BigDecimal("1.00");
        Account account = new Account("123456789", amount.doubleValue());
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.withdraw(new WithdrawRequest(accountNumber, amountToWithdraw.toString())));

        System.out.println(exception.getErrors().get("amount"));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.INSUFFICIENT_BALANCE));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Failure_AccountWithNegativeBalance() {
        // Arrange
        String accountNumber = "123456789";
        BigDecimal amount = new BigDecimal("-1.00");
        BigDecimal amountToWithdraw = new BigDecimal("1.00");
        Account account = new Account("123456789", amount.doubleValue());
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        // Act
        ExceptionList exception = assertThrows(ExceptionList.class, () -> ledgerService.withdraw(new WithdrawRequest(accountNumber, amountToWithdraw.toString())));

        // Assert
        assertTrue(exception.getErrors().get("amount").toString().contains(Messages.INSUFFICIENT_BALANCE));
        verify(accountRepository, never()).save(any(Account.class));
    }
}