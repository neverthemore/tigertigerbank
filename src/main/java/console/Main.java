package console;

import analytics.AnalyticsService;
import di.DIContainer;
import domain.BankAccount;
import domain.Category;
import domain.Operation;
import io.csv.CSVOperationExporter;
import io.csv.CSVOperationImporter;
import io.json.JSONOperationExporter;
import io.json.JSONOperationImporter;
import io.yaml.YAMLOperationExporter;
import io.yaml.YAMLOperationImporter;
import repository.*;
import repository.inmemory.InMemoryAccountRepository;
import repository.inmemory.InMemoryCategoryRepository;
import repository.inmemory.InMemoryOperationRepository;
import service.AccountService;
import service.CategoryService;
import service.OperationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Инициализация DI-контейнера
        DIContainer container = new DIContainer();

        // Регистрация репозиториев как синглтонов
        AccountRepository accountRepo = new InMemoryAccountRepository();
        CategoryRepository categoryRepo = new InMemoryCategoryRepository();
        OperationRepository operationRepo = new InMemoryOperationRepository();

        container.registerSingleton(AccountRepository.class, accountRepo);
        container.registerSingleton(CategoryRepository.class, categoryRepo);
        container.registerSingleton(OperationRepository.class, operationRepo);

        // Регистрация сервисов (каждый раз новый экземпляр)
        container.register(AccountService.class, () -> new AccountService(
                container.resolve(AccountRepository.class)
        ));
        container.register(CategoryService.class, () -> new CategoryService(
                container.resolve(CategoryRepository.class)
        ));
        container.register(OperationService.class, () -> new OperationService(
                container.resolve(OperationRepository.class),
                container.resolve(AccountRepository.class),
                container.resolve(CategoryRepository.class)
        ));
        container.register(AnalyticsService.class, () -> new AnalyticsService(
                container.resolve(OperationRepository.class),
                container.resolve(CategoryRepository.class)
        ));

        // Получение сервисов из контейнера
        AccountService accountService = container.resolve(AccountService.class);
        CategoryService categoryService = container.resolve(CategoryService.class);
        OperationService operationService = container.resolve(OperationService.class);
        AnalyticsService analyticsService = container.resolve(AnalyticsService.class);

        // Демонстрация работы
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Меню ===");
            System.out.println("1. Создать счет");
            System.out.println("2. Создать категорию");
            System.out.println("3. Создать операцию");
            System.out.println("4. Показать все счета");
            System.out.println("5. Показать все категории");
            System.out.println("6. Показать все операции");
            System.out.println("7. Аналитика за период");
            System.out.println("8. Экспорт операций в JSON");
            System.out.println("9. Импорт операций из JSON");
            System.out.println("10. Экспорт операций в YAML");
            System.out.println("11. Импорт операций из YAML");
            System.out.println("12. Экспорт операций в CSV");
            System.out.println("13. Импорт операций из CSV");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Название счета: ");
                        String accName = scanner.nextLine();
                        System.out.print("Начальный баланс: ");
                        double balance = scanner.nextDouble();
                        scanner.nextLine();
                        BankAccount account = accountService.createAccount(accName, balance);
                        System.out.println("Счет создан: " + account.getId() + ", " + account.getName() + ", баланс: " + account.getBalance());
                        break;

                    case 2:
                        System.out.print("Тип (INCOME/EXPENSE): ");
                        String typeStr = scanner.nextLine().toUpperCase();
                        Category.Type type = Category.Type.valueOf(typeStr);
                        System.out.print("Название категории: ");
                        String catName = scanner.nextLine();
                        Category category = categoryService.createCategory(type, catName);
                        System.out.println("Категория создана: " + category.getId() + ", " + category.getType() + ", " + category.getName());
                        break;

                    case 3:
                        System.out.print("Тип операции (INCOME/EXPENSE): ");
                        String opTypeStr = scanner.nextLine().toUpperCase();
                        Category.Type opType = Category.Type.valueOf(opTypeStr);
                        System.out.print("ID счета: ");
                        String accId = scanner.nextLine();
                        System.out.print("Сумма: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Дата (гггг-мм-дд): ");
                        String dateStr = scanner.nextLine();
                        LocalDate date = LocalDate.parse(dateStr);
                        System.out.print("Описание: ");
                        String desc = scanner.nextLine();
                        System.out.print("ID категории: ");
                        String catId = scanner.nextLine();

                        Operation op = operationService.createOperation(opType, accId, amount, date, desc, catId);
                        System.out.println("Операция создана: " + op.getId());
                        break;

                    case 4:
                        List<BankAccount> accounts = accountService.getAllAccounts();
                        System.out.println("Счета:");
                        accounts.forEach(a -> System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()));
                        break;

                    case 5:
                        List<Category> categories = categoryService.getAllCategories();
                        System.out.println("Категории:");
                        categories.forEach(c -> System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()));
                        break;

                    case 6:
                        List<Operation> operations = operationService.getAllOperations();
                        System.out.println("Операции:");
                        operations.forEach(o -> System.out.println(o.getId() + " | " + o.getType() + " | " + o.getAmount() + " | " + o.getDate()));
                        break;

                    case 7:
                        System.out.print("Начальная дата (гггг-мм-дд): ");
                        String fromStr = scanner.nextLine();
                        LocalDate from = LocalDate.parse(fromStr);
                        System.out.print("Конечная дата (гггг-мм-дд): ");
                        String toStr = scanner.nextLine();
                        LocalDate to = LocalDate.parse(toStr);

                        double diff = analyticsService.getIncomeExpenseDifference(from, to);
                        System.out.println("Разница доходов и расходов: " + diff);

                        Map<Category, Double> incomeByCat = analyticsService.getIncomeByCategory(from, to);
                        System.out.println("Доходы по категориям:");
                        incomeByCat.forEach((cat, sum) -> System.out.println("  " + cat.getName() + ": " + sum));

                        Map<Category, Double> expenseByCat = analyticsService.getExpenseByCategory(from, to);
                        System.out.println("Расходы по категориям:");
                        expenseByCat.forEach((cat, sum) -> System.out.println("  " + cat.getName() + ": " + sum));
                        break;

                    case 8:
                        JSONOperationExporter jsonExporter = new JSONOperationExporter();
                        jsonExporter.export(operationService.getAllOperations(), "operations.json");
                        System.out.println("Операции экспортированы в operations.json");
                        break;

                    case 9:
                        JSONOperationImporter jsonImporter = new JSONOperationImporter();
                        List<Operation> importedJson = jsonImporter.importData("operations.json");
                        for (Operation o : importedJson) {
                            if (!operationService.getAllOperations().stream().anyMatch(op -> op.getId().equals(o.getId()))) {
                                operationService.createOperation(o.getType(), o.getBankAccountId(), o.getAmount(),
                                        o.getDate(), o.getDescription(), o.getCategoryId());
                            }
                        }
                        System.out.println("Импортировано операций из JSON: " + importedJson.size());
                        break;

                    case 10:
                        YAMLOperationExporter yamlExporter = new YAMLOperationExporter();
                        yamlExporter.export(operationService.getAllOperations(), "operations.yaml");
                        System.out.println("Операции экспортированы в operations.yaml");
                        break;

                    case 11:
                        YAMLOperationImporter yamlImporter = new YAMLOperationImporter();
                        List<Operation> importedYaml = yamlImporter.importData("operations.yaml");
                        for (Operation o : importedYaml) {
                            if (!operationService.getAllOperations().stream().anyMatch(op -> op.getId().equals(o.getId()))) {
                                operationService.createOperation(o.getType(), o.getBankAccountId(), o.getAmount(),
                                        o.getDate(), o.getDescription(), o.getCategoryId());
                            }
                        }
                        System.out.println("Импортировано операций из YAML: " + importedYaml.size());
                        break;

                    case 12:
                        CSVOperationExporter csvExporter = new CSVOperationExporter();
                        csvExporter.export(operationService.getAllOperations(), "operations.csv");
                        System.out.println("Операции экспортированы в operations.csv");
                        break;

                    case 13:
                        CSVOperationImporter csvImporter = new CSVOperationImporter();
                        List<Operation> importedCsv = csvImporter.importData("operations.csv");
                        for (Operation o : importedCsv) {
                            if (!operationService.getAllOperations().stream().anyMatch(op -> op.getId().equals(o.getId()))) {
                                operationService.createOperation(o.getType(), o.getBankAccountId(), o.getAmount(),
                                        o.getDate(), o.getDescription(), o.getCategoryId());
                            }
                        }
                        System.out.println("Импортировано операций из CSV: " + importedCsv.size());
                        break;

                    case 0:
                        exit = true;
                        break;

                    default:
                        System.out.println("Неверный выбор");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }
}