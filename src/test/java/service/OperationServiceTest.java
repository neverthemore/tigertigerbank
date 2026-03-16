package service;

import domain.BankAccount;
import domain.Category;
import domain.Operation;
import factory.*;
import repository.*;
import repository.inmemory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceTest {

    private OperationService operationService;
    private AccountRepository accountRepository;
    private CategoryRepository categoryRepository;
    private OperationRepository operationRepository;
    private BankAccountFactory accountFactory;
    private CategoryFactory categoryFactory;
    private OperationFactory operationFactory;

    private BankAccount testAccount;
    private Category incomeCategory;
    private Category expenseCategory;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        categoryRepository = new InMemoryCategoryRepository();
        operationRepository = new InMemoryOperationRepository();

        accountFactory = new DefaultBankAccountFactory();
        categoryFactory = new DefaultCategoryFactory();
        operationFactory = new DefaultOperationFactory();

        operationService = new OperationService(
                operationRepository,
                accountRepository,
                categoryRepository,
                operationFactory
        );

        testAccount = accountRepository.save(accountFactory.create("Test", 1000));
        incomeCategory = categoryRepository.save(categoryFactory.create(Category.Type.INCOME, "Salary"));
        expenseCategory = categoryRepository.save(categoryFactory.create(Category.Type.EXPENSE, "Food"));
    }

    @Test
    void createIncomeOperation_IncreasesBalance() {
        Operation op = operationService.createOperation(
                Category.Type.INCOME,
                testAccount.getId(),
                500,
                LocalDate.now(),
                "Monthly salary",
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
                "Groceries",
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
                        "Invalid",
                        incomeCategory.getId()
                )
        );
    }

    @Test
    void createOperation_NonExistingAccount_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.INCOME,
                        "wrong-id",
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
                        "wrong-cat"
                )
        );
    }

    @Test
    void createOperation_CategoryTypeMismatch_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.createOperation(
                        Category.Type.EXPENSE,
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
        double balanceAfterDeletion = accountRepository.findById(testAccount.getId()).get().getBalance(); // 1000

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

    @Test
    void recalcBalance_WhenMismatch_ShouldCorrect() {
        operationService.createOperation(
                Category.Type.INCOME,
                testAccount.getId(),
                500,
                LocalDate.now(),
                "Salary",
                incomeCategory.getId()
        );
        operationService.createOperation(
                Category.Type.EXPENSE,
                testAccount.getId(),
                200,
                LocalDate.now(),
                "Food",
                expenseCategory.getId()
        );

        BankAccount account = accountRepository.findById(testAccount.getId()).get();
        account.setBalance(9999);
        accountRepository.save(account);

        operationService.recalcBalance(testAccount.getId());

        assertEquals(1300, accountRepository.findById(testAccount.getId()).get().getBalance());
    }
}