package facade;

import domain.Category;
import domain.Operation;
import service.OperationService;
import java.time.LocalDate;
import java.util.List;

public class OperationFacade {
    private final OperationService operationService;

    public OperationFacade(OperationService operationService) {
        this.operationService = operationService;
    }

    public Operation createOperation(Category.Type type, String bankAccountId, double amount,
                                     LocalDate date, String description, String categoryId) {
        return operationService.createOperation(type, bankAccountId, amount, date, description, categoryId);
    }

    public Operation getOperation(String id) {
        return operationService.getOperation(id);
    }

    public List<Operation> getAllOperations() {
        return operationService.getAllOperations();
    }

    public void deleteOperation(String id) {
        operationService.deleteOperation(id);
    }

    public void recalcBalance(String accountId) {
        operationService.recalcBalance(accountId);
    }
}