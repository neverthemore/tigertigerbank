package factory;

import domain.Category;
import domain.Operation;
import java.time.LocalDate;

public interface OperationFactory {
    Operation create(Category.Type type, String bankAccountId, double amount,
                     LocalDate date, String description, String categoryId);
}