package service;

import domain.BankAccount;
import repository.AccountRepository;
import repository.inmemory.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        accountService = new AccountService(accountRepository);
    }

    @Test
    void createAccount_ValidData_ShouldSave() {
        BankAccount account = accountService.createAccount("Test", 100);
        assertNotNull(account.getId());
        assertEquals("Test", account.getName());
        assertEquals(100, account.getBalance());
        assertTrue(accountRepository.findById(account.getId()).isPresent());
    }

    @Test
    void createAccount_NegativeBalance_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount("Test", -10));
    }

    @Test
    void getAccount_ExistingId_ReturnsAccount() {
        BankAccount saved = accountService.createAccount("Test", 100);
        BankAccount found = accountService.getAccount(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void getAccount_NonExistingId_Throws() {
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccount("non-existent"));
    }

    @Test
    void getAllAccounts_ReturnsAll() {
        accountService.createAccount("A", 100);
        accountService.createAccount("B", 200);
        List<BankAccount> accounts = accountService.getAllAccounts();
        assertEquals(2, accounts.size());
    }

    @Test
    void updateAccount_ChangesName() {
        BankAccount account = accountService.createAccount("Old", 100);
        accountService.updateAccount(account.getId(), "New");
        BankAccount updated = accountService.getAccount(account.getId());
        assertEquals("New", updated.getName());
    }

    @Test
    void deleteAccount_Removes() {
        BankAccount account = accountService.createAccount("Test", 100);
        accountService.deleteAccount(account.getId());
        assertFalse(accountRepository.exists(account.getId()));
    }

    @Test
    void deposit_IncreasesBalance() {
        BankAccount account = accountService.createAccount("Test", 100);
        accountService.deposit(account.getId(), 50);
        assertEquals(150, accountService.getAccount(account.getId()).getBalance());
    }

    @Test
    void withdraw_DecreasesBalance() {
        BankAccount account = accountService.createAccount("Test", 100);
        accountService.withdraw(account.getId(), 30);
        assertEquals(70, accountService.getAccount(account.getId()).getBalance());
    }

    @Test
    void withdraw_InsufficientFunds_Throws() {
        BankAccount account = accountService.createAccount("Test", 100);
        assertThrows(IllegalStateException.class, () -> accountService.withdraw(account.getId(), 200));
    }
}