package analytics;

import domain.Category;
import domain.Operation;
import repository.CategoryRepository;
import repository.OperationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;

    public AnalyticsService(OperationRepository operationRepository, CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
      Разница между доходами и расходами за период.
     */
    public double getIncomeExpenseDifference(LocalDate from, LocalDate to) {
        List<Operation> operations = operationRepository.findAll().stream()
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .toList();

        double totalIncome = operations.stream()
                .filter(op -> op.getType() == Category.Type.INCOME)
                .mapToDouble(Operation::getAmount)
                .sum();

        double totalExpense = operations.stream()
                .filter(op -> op.getType() == Category.Type.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();

        return totalIncome - totalExpense;
    }

    /**
      Группировка доходов по категориям.
     */
    public Map<Category, Double> getIncomeByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepository.findAll().stream()
                .filter(op -> op.getType() == Category.Type.INCOME)
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .toList());
    }

    /**
      Группировка расходов по категориям.
     */
    public Map<Category, Double> getExpenseByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepository.findAll().stream()
                .filter(op -> op.getType() == Category.Type.EXPENSE)
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .toList());
    }

    private Map<Category, Double> groupByCategory(List<Operation> operations) {
        return operations.stream()
                .collect(Collectors.groupingBy(
                        op -> categoryRepository.findById(op.getCategoryId())
                                .orElseThrow(() -> new IllegalStateException("Категория не найдена для операции")),
                        Collectors.summingDouble(Operation::getAmount)
                ));
    }
}