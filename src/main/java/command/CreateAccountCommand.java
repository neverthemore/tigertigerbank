package command;

import facade.AccountFacade;
import java.util.Scanner;

public class CreateAccountCommand implements Command {
    private final AccountFacade accountFacade;
    private final Scanner scanner;

    public CreateAccountCommand(AccountFacade accountFacade, Scanner scanner) {
        this.accountFacade = accountFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введите название счёта: ");
        String name = scanner.nextLine();
        System.out.print("Введите начальный баланс: ");
        double balance = Double.parseDouble(scanner.nextLine());
        var account = accountFacade.createAccount(name, balance);
        System.out.println("Счёт создан. ID: " + account.getId());
    }
}