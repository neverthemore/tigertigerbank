package command;

import facade.AnalyticsFacade;
import java.time.LocalDate;
import java.util.Scanner;

public class GetAnalyticsCommand implements Command {
    private final AnalyticsFacade analyticsFacade;
    private final Scanner scanner;

    public GetAnalyticsCommand(AnalyticsFacade analyticsFacade, Scanner scanner) {
        this.analyticsFacade = analyticsFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Начальная дата (ГГГГ-ММ-ДД): ");
        LocalDate from = LocalDate.parse(scanner.nextLine());
        System.out.print("Конечная дата (ГГГГ-ММ-ДД): ");
        LocalDate to = LocalDate.parse(scanner.nextLine());

        double diff = analyticsFacade.getDifference(from, to);
        System.out.println("Разница доходов/расходов: " + diff);
        System.out.println("Доходы по категориям:");
        analyticsFacade.getIncomeByCategory(from, to).forEach((cat, sum) ->
                System.out.println("  " + cat.getName() + ": " + sum));
        System.out.println("Расходы по категориям:");
        analyticsFacade.getExpenseByCategory(from, to).forEach((cat, sum) ->
                System.out.println("  " + cat.getName() + ": " + sum));
    }
}