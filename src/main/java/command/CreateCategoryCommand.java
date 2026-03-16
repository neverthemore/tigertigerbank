package command;

import domain.Category;
import facade.CategoryFacade;
import java.util.Scanner;

public class CreateCategoryCommand implements Command {
    private final CategoryFacade categoryFacade;
    private final Scanner scanner;

    public CreateCategoryCommand(CategoryFacade categoryFacade, Scanner scanner) {
        this.categoryFacade = categoryFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Тип категории (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine().toUpperCase();
        Category.Type type = Category.Type.valueOf(typeStr);
        System.out.print("Название категории: ");
        String name = scanner.nextLine();
        var category = categoryFacade.createCategory(type, name);
        System.out.println("Категория создана. ID: " + category.getId());
    }
}