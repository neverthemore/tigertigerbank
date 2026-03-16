package console;

import di.DIContainer;
import domain.Category;
import exporter.*;
import facade.*;
import factory.*;
import importer.*;
import repository.*;
import repository.inmemory.*;
import service.*;
import java.time.LocalDate;
import java.util.Scanner;
import command.*;

public class Main {
    public static void main(String[] args) {
        DIContainer container = new DIContainer();

        // Репозитории (синглтоны)
        container.registerSingleton(AccountRepository.class, new InMemoryAccountRepository());
        container.registerSingleton(CategoryRepository.class, new InMemoryCategoryRepository());
        container.registerSingleton(OperationRepository.class, new InMemoryOperationRepository());

        // Фабрики (синглтоны)
        container.registerSingleton(BankAccountFactory.class, new DefaultBankAccountFactory());
        container.registerSingleton(CategoryFactory.class, new DefaultCategoryFactory());
        container.registerSingleton(OperationFactory.class, new DefaultOperationFactory());

        // Сервисы
        container.register(AccountService.class, () -> new AccountService(
                container.resolve(AccountRepository.class),
                container.resolve(BankAccountFactory.class)
        ));
        container.register(CategoryService.class, () -> new CategoryService(
                container.resolve(CategoryRepository.class),
                container.resolve(CategoryFactory.class)
        ));
        container.register(OperationService.class, () -> new OperationService(
                container.resolve(OperationRepository.class),
                container.resolve(AccountRepository.class),
                container.resolve(CategoryRepository.class),
                container.resolve(OperationFactory.class)
        ));

        // Фасады
        container.register(AccountFacade.class, () -> new AccountFacade(
                container.resolve(AccountService.class)
        ));
        container.register(CategoryFacade.class, () -> new CategoryFacade(
                container.resolve(CategoryService.class)
        ));
        container.register(OperationFacade.class, () -> new OperationFacade(
                container.resolve(OperationService.class)
        ));
        container.register(AnalyticsFacade.class, () -> new AnalyticsFacade(
                container.resolve(OperationRepository.class),
                container.resolve(CategoryRepository.class)
        ));

        // Импортёры/экспортёры
        OperationService opService = container.resolve(OperationService.class);
        JsonImporter jsonImporter = new JsonImporter(opService);
        YamlImporter yamlImporter = new YamlImporter(opService);
        CsvImporter csvImporter = new CsvImporter(opService);
        JsonExporter jsonExporter = new JsonExporter();
        YamlExporter yamlExporter = new YamlExporter();
        CsvExporter csvExporter = new CsvExporter();
        ImportExportFacade importExportFacade = new ImportExportFacade(
                jsonImporter, yamlImporter, csvImporter,
                jsonExporter, yamlExporter, csvExporter
        );
        container.registerSingleton(ImportExportFacade.class, importExportFacade);

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1. Создать счёт");
            System.out.println("2. Создать категорию");
            System.out.println("3. Создать операцию");
            System.out.println("4. Показать все счета");
            System.out.println("5. Показать все категории");
            System.out.println("6. Показать все операции");
            System.out.println("7. Аналитика за период");
            System.out.println("8. Экспорт в JSON");
            System.out.println("9. Импорт из JSON");
            System.out.println("10. Экспорт в YAML");
            System.out.println("11. Импорт из YAML");
            System.out.println("12. Экспорт в CSV");
            System.out.println("13. Импорт из CSV");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        new TimedCommand(new CreateAccountCommand(container.resolve(AccountFacade.class), scanner)).execute();
                        break;
                    case 2:
                        new TimedCommand(new CreateCategoryCommand(container.resolve(CategoryFacade.class), scanner)).execute();
                        break;
                    case 3:
                        new TimedCommand(new CreateOperationCommand(
                                container.resolve(OperationFacade.class),
                                container.resolve(AccountFacade.class),
                                container.resolve(CategoryFacade.class),
                                scanner
                        )).execute();
                        break;
                    case 4:
                        container.resolve(AccountFacade.class).getAllAccounts().forEach(a ->
                                System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()));
                        break;
                    case 5:
                        container.resolve(CategoryFacade.class).getAllCategories().forEach(c ->
                                System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()));
                        break;
                    case 6:
                        container.resolve(OperationFacade.class).getAllOperations().forEach(o ->
                                System.out.println(o.getId() + " | " + o.getType() + " | " + o.getAmount() + " | " + o.getDate()));
                        break;
                    case 7:
                        new TimedCommand(new GetAnalyticsCommand(container.resolve(AnalyticsFacade.class), scanner)).execute();
                        break;
                    case 8:
                        new TimedCommand(new ExportCommand(importExportFacade, container.resolve(OperationFacade.class), "json")).execute();
                        break;
                    case 9:
                        new TimedCommand(new ImportCommand(importExportFacade, "json")).execute();
                        break;
                    case 10:
                        new TimedCommand(new ExportCommand(importExportFacade, container.resolve(OperationFacade.class), "yaml")).execute();
                        break;
                    case 11:
                        new TimedCommand(new ImportCommand(importExportFacade, "yaml")).execute();
                        break;
                    case 12:
                        new TimedCommand(new ExportCommand(importExportFacade, container.resolve(OperationFacade.class), "csv")).execute();
                        break;
                    case 13:
                        new TimedCommand(new ImportCommand(importExportFacade, "csv")).execute();
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