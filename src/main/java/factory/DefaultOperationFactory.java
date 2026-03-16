package factory;

import domain.Category;
import domain.Operation;
import java.time.LocalDate;
import java.util.UUID;

public class DefaultOperationFactory implements OperationFactory {
    @Override
    public Operation create(Category.Type type, String bankAccountId, double amount,
                            LocalDate date, String description, String categoryId) {
        return new Operation(UUID.randomUUID().toString(), type, bankAccountId, amount, date, description, categoryId);
    }
}