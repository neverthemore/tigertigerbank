package service;

import domain.Category;
import factory.CategoryFactory;
import factory.DefaultCategoryFactory;
import repository.CategoryRepository;
import repository.inmemory.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    private CategoryFactory categoryFactory;

    @BeforeEach
    void setUp() {
        categoryRepository = new InMemoryCategoryRepository();
        categoryFactory = new DefaultCategoryFactory();
        categoryService = new CategoryService(categoryRepository, categoryFactory);
    }

    @Test
    void createCategory_ValidData_ShouldSave() {
        Category category = categoryService.createCategory(Category.Type.INCOME, "Salary");
        assertNotNull(category.getId());
        assertEquals(Category.Type.INCOME, category.getType());
        assertEquals("Salary", category.getName());
        assertTrue(categoryRepository.findById(category.getId()).isPresent());
    }

    @Test
    void createCategory_EmptyName_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(Category.Type.EXPENSE, ""));
    }

    @Test
    void getCategory_ExistingId_ReturnsCategory() {
        Category saved = categoryService.createCategory(Category.Type.INCOME, "Salary");
        Category found = categoryService.getCategory(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void getCategory_NonExistingId_Throws() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategory("non-existent"));
    }

    @Test
    void getAllCategories_ReturnsAll() {
        categoryService.createCategory(Category.Type.INCOME, "Salary");
        categoryService.createCategory(Category.Type.EXPENSE, "Food");
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    void updateCategory_ChangesName() {
        Category category = categoryService.createCategory(Category.Type.INCOME, "Old");
        categoryService.updateCategory(category.getId(), "New");
        Category updated = categoryService.getCategory(category.getId());
        assertEquals("New", updated.getName());
    }

    @Test
    void deleteCategory_Removes() {
        Category category = categoryService.createCategory(Category.Type.INCOME, "Test");
        categoryService.deleteCategory(category.getId());
        assertFalse(categoryRepository.exists(category.getId()));
    }
}