package service;

import domain.BankAccount;
import domain.Category;
import domain.Operation;
import repository.AccountRepository;
import repository.CategoryRepository;
import repository.OperationRepository;
import java.time.LocalDate;
import java.util.List;

public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public OperationService(OperationRepository operationRepository,
                            AccountRepository accountRepository,
                            CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Operation createOperation(Category.Type type, String bankAccountId, double amount,
                                     LocalDate date, String description, String categoryId) {
        // Валидация
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        BankAccount account = accountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Банковский счет не найден"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        if (category.getType() != type) {
            throw new IllegalArgumentException("Тип категории не соответствует типу операции");
        }

        Operation operation = new Operation(type, bankAccountId, amount, date, description, categoryId);
        operationRepository.save(operation);

        // Обновление баланса счета
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
                .orElseThrow(() -> new IllegalArgumentException("Операция не найдена с идентификатором: " + id));
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public void deleteOperation(String id) {
        Operation operation = getOperation(id);
        // Откат влияния на баланс (для целостности)
        BankAccount account = accountRepository.findById(operation.getBankAccountId())
                .orElseThrow(() -> new IllegalStateException("Связанная учетная запись не найдена"));
        if (operation.getType() == Category.Type.INCOME) {
            account.withdraw(operation.getAmount()); // обратная операция
        } else {
            account.deposit(operation.getAmount());
        }
        accountRepository.save(account);
        operationRepository.delete(id);
    }
}