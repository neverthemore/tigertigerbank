package service;

import domain.BankAccount;
import domain.Category;
import domain.Operation;
import factory.OperationFactory;
import repository.AccountRepository;
import repository.CategoryRepository;
import repository.OperationRepository;
import java.time.LocalDate;
import java.util.List;

public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationFactory operationFactory;

    public OperationService(OperationRepository operationRepository,
                            AccountRepository accountRepository,
                            CategoryRepository categoryRepository,
                            OperationFactory operationFactory) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationFactory = operationFactory;
    }

    public Operation createOperation(Category.Type type, String bankAccountId, double amount,
                                     LocalDate date, String description, String categoryId) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        BankAccount account = accountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (category.getType() != type) throw new IllegalArgumentException("Category type mismatch");

        Operation operation = operationFactory.create(type, bankAccountId, amount, date, description, categoryId);
        operationRepository.save(operation);

        if (type == Category.Type.INCOME) {
            account.deposit(amount);
        } else {
            account.withdraw(amount);
        }
        accountRepository.save(account);

        return operation;
    }

    public Operation getOperation(String id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public void deleteOperation(String id) {
        Operation operation = getOperation(id);
        BankAccount account = accountRepository.findById(operation.getBankAccountId())
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        if (operation.getType() == Category.Type.INCOME) {
            account.withdraw(operation.getAmount());
        } else {
            account.deposit(operation.getAmount());
        }
        accountRepository.save(account);
        operationRepository.delete(id);
    }

    public void recalcBalance(String accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        double net = operationRepository.findAll().stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .mapToDouble(op -> op.getType() == Category.Type.INCOME ? op.getAmount() : -op.getAmount())
                .sum();
        double correctBalance = account.getInitialBalance() + net; // начальный баланс + нетто-изменения
        if (Math.abs(account.getBalance() - correctBalance) > 0.001) {
            account.setBalance(correctBalance);
            accountRepository.save(account);
        }
    }
}