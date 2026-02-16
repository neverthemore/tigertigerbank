package service;

import domain.BankAccount;
import repository.AccountRepository;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");
        }
        BankAccount account = new BankAccount(name, initialBalance);
        return accountRepository.save(account);
    }

    public BankAccount getAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Учетная запись с идентификатором не найдена: " + id));
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
        if (!accountRepository.exists(id)) {
            throw new IllegalArgumentException("Учетная запись с идентификатором не найдена: " + id);
        }
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