package facade;

import domain.Category;
import domain.Operation;
import repository.CategoryRepository;
import repository.OperationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsFacade {
    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;

    public AnalyticsFacade(OperationRepository operationRepository, CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }

    public double getDifference(LocalDate from, LocalDate to) {
        List<Operation> ops = operationRepository.findAll().stream()
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .toList();
        double income = ops.stream().filter(o -> o.getType() == Category.Type.INCOME).mapToDouble(Operation::getAmount).sum();
        double expense = ops.stream().filter(o -> o.getType() == Category.Type.EXPENSE).mapToDouble(Operation::getAmount).sum();
        return income - expense;
    }

    public Map<Category, Double> getIncomeByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepository.findAll().stream()
                .filter(o -> o.getType() == Category.Type.INCOME)
                .filter(o -> !o.getDate().isBefore(from) && !o.getDate().isAfter(to))
                .toList());
    }

    public Map<Category, Double> getExpenseByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepository.findAll().stream()
                .filter(o -> o.getType() == Category.Type.EXPENSE)
                .filter(o -> !o.getDate().isBefore(from) && !o.getDate().isAfter(to))
                .toList());
    }

    private Map<Category, Double> groupByCategory(List<Operation> operations) {
        return operations.stream()
                .collect(Collectors.groupingBy(
                        op -> categoryRepository.findById(op.getCategoryId()).orElseThrow(),
                        Collectors.summingDouble(Operation::getAmount)
                ));
    }
}