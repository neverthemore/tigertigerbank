package service;

import domain.Category;
import factory.CategoryFactory;
import repository.CategoryRepository;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryFactory categoryFactory;

    public CategoryService(CategoryRepository categoryRepository, CategoryFactory categoryFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryFactory = categoryFactory;
    }

    public Category createCategory(Category.Type type, String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        Category category = categoryFactory.create(type, name);
        return categoryRepository.save(category);
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
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
        categoryRepository.delete(id);
    }
}