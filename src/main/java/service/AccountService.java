package service;

import domain.BankAccount;
import factory.BankAccountFactory;
import repository.AccountRepository;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    private final BankAccountFactory accountFactory;

    public AccountService(AccountRepository accountRepository, BankAccountFactory accountFactory) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        BankAccount account = accountFactory.create(name, initialBalance);
        return accountRepository.save(account);
    }

    public BankAccount getAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void updateAccount(String id, String newName) {
        BankAccount account = getAccount(id);
        account.setName(newName);
        accountRepository.save(account);
    }

    public void deleteAccount(String id) {
        accountRepository.delete(id);
    }

    public void deposit(String id, double amount) {
        BankAccount account = getAccount(id);
        account.deposit(amount);
        accountRepository.save(account);
    }

    public void withdraw(String id, double amount) {
        BankAccount account = getAccount(id);
        account.withdraw(amount);
        accountRepository.save(account);
    }
}