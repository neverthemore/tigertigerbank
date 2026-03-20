package facade;

import domain.BankAccount;
import service.AccountService;
import java.util.List;

public class AccountFacade {
    private final AccountService accountService;

    public AccountFacade(AccountService accountService) {
        this.accountService = accountService;
    }

    public BankAccount createAccount(String name, double balance) {
        return accountService.createAccount(name, balance);
    }

    public BankAccount getAccount(String id) {
        return accountService.getAccount(id);
    }

    public List<BankAccount> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    public void updateAccount(String id, String newName) {
        accountService.updateAccount(id, newName);
    }

    public void deleteAccount(String id) {
        accountService.deleteAccount(id);
    }

    public void deposit(String id, double amount) {
        accountService.deposit(id, amount);
    }

    public void withdraw(String id, double amount) {
        accountService.withdraw(id, amount);
    }
}