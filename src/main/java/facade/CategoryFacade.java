package facade;

import domain.Category;
import service.CategoryService;
import java.util.List;

public class CategoryFacade {
    private final CategoryService categoryService;

    public CategoryFacade(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Category createCategory(Category.Type type, String name) {
        return categoryService.createCategory(type, name);
    }

    public Category getCategory(String id) {
        return categoryService.getCategory(id);
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public void updateCategory(String id, String newName) {
        categoryService.updateCategory(id, newName);
    }

    public void deleteCategory(String id) {
        categoryService.deleteCategory(id);
    }
}