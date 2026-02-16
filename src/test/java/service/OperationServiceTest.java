package service;

import domain.BankAccount;
import domain.Category;
import domain.Operation;
import repository.AccountRepository;
import repository.CategoryRepository;
import repository.OperationRepository;
import repository.inmemory.InMemoryAccountRepository;
import repository.inmemory.InMemoryCategoryRepository;
import repository.inmemory.InMemoryOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceTest {
    private OperationService operationService;
    private AccountRepository accountRepository;
    private CategoryRepository categoryRepository;
    private OperationRepository operationRepository;
    private BankAccount testAccount;
    private Category incomeCategory;
    private Category expenseCategory;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        categoryRepository = new InMemoryCategoryRepository();
        operationRepository = new InMemoryOperationRepository();
        operationService = new OperationService(operationRepository, accountRepository, categoryRepository);

        testAccount = accountRepository.save(new BankAccount("Test", 1000));
        incomeCategory = categoryRepository.save(new Category(Category.Type.INCOME, "Зарплата"));
        expenseCategory = categoryRepository.save(new Category(Category.Type.EXPENSE, "Еда"));
    }

    @Test
    void createIncomeOperation_IncreasesBalance() {
        Operation op = operationService.createOperation(
                Category.Type.INCOME,
                testAccount.getId(),
                500,
                LocalDate.now(),
                "Месячная зарплат",
                incomeCategory.getId()
        );

        assertNotNull(op.getId());
        assertEquals(1500, accountRepository.findById(testAccount.getId()).get().getBalance());
        assertTrue(operationRepository.findById(op.getId()).isPresent());
    }

    @Test
    void createExpenseOperation_DecreasesBalance() {
        Operation op = operationService.createOperation(
                Category.Type.EXPENSE,
                testAccount.getId(),
                200,
                LocalDate.now(),
                "Бакалея",
                expenseCategory.getId()
        );

        assertEquals(800, accountRepository.findById(testAccount.getId()).get().getBalance());
        assertTrue(operationRepository.findById(op.getId()).isPresent());
    }

    @Test
    void createOperation_InvalidAmount_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.INCOME,
                        testAccount.getId(),
                        -100,
                        LocalDate.now(),
                        "недействительно",
                        incomeCategory.getId()
                )
        );
    }

    @Test
    void createOperation_NonExistingAccount_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.INCOME,
                        "неправильный id",
                        100,
                        LocalDate.now(),
                        "Test",
                        incomeCategory.getId()
                )
        );
    }

    @Test
    void createOperation_NonExistingCategory_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.INCOME,
                        testAccount.getId(),
                        100,
                        LocalDate.now(),
                        "Test",
                        "неправильный кот"
                )
        );
    }

    @Test
    void createOperation_CategoryTypeMismatch_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.EXPENSE, // expense operation with income category
                        testAccount.getId(),
                        100,
                        LocalDate.now(),
                        "Test",
                        incomeCategory.getId()
                )
        );
    }

    @Test
    void deleteOperation_RevertsBalance() {
        Operation op = operationService.createOperation(
                Category.Type.EXPENSE,
                testAccount.getId(),
                200,
                LocalDate.now(),
                "Test",
                expenseCategory.getId()
        );
        double balanceAfterCreation = accountRepository.findById(testAccount.getId()).get().getBalance(); // 800

        operationService.deleteOperation(op.getId());
        double balanceAfterDeletion = accountRepository.findById(testAccount.getId()).get().getBalance(); // should be 1000

        assertEquals(1000, balanceAfterDeletion);
        assertFalse(operationRepository.findById(op.getId()).isPresent());
    }

    @Test
    void getOperation_ReturnsCorrect() {
        Operation saved = operationService.createOperation(
                Category.Type.INCOME,
                testAccount.getId(),
                300,
                LocalDate.now(),
                "Test",
                incomeCategory.getId()
        );
        Operation found = operationService.getOperation(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }
}