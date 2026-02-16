package service;

import domain.Category;
import repository.CategoryRepository;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category.Type type, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }
        Category category = new Category(type, name);
        return categoryRepository.save(category);
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с идентификатором не найдена: " + id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void updateCategory(String id, String newName) {
        Category category = getCategory(id);
        category.setName(newName);
        categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        if (!categoryRepository.exists(id)) {
            throw new IllegalArgumentException("Категория с идентификатором не найдена: " + id);
        }
        categoryRepository.delete(id);
    }
}