package command;

import domain.Category;
import facade.AccountFacade;
import facade.CategoryFacade;
import facade.OperationFacade;
import java.time.LocalDate;
import java.util.Scanner;

public class CreateOperationCommand implements Command {
    private final OperationFacade operationFacade;
    private final AccountFacade accountFacade;
    private final CategoryFacade categoryFacade;
    private final Scanner scanner;

    public CreateOperationCommand(OperationFacade operationFacade, AccountFacade accountFacade,
                                  CategoryFacade categoryFacade, Scanner scanner) {
        this.operationFacade = operationFacade;
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Тип операции (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine().toUpperCase();
        Category.Type type = Category.Type.valueOf(typeStr);
        System.out.print("ID счёта: ");
        String accId = scanner.nextLine();
        System.out.print("Сумма: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Дата (ГГГГ-ММ-ДД): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Описание: ");
        String desc = scanner.nextLine();
        System.out.print("ID категории: ");
        String catId = scanner.nextLine();

        var op = operationFacade.createOperation(type, accId, amount, date, desc, catId);
        System.out.println("Операция создана. ID: " + op.getId());
    }
}