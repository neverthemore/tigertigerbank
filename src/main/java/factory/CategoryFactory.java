package factory;

import domain.Category;

public interface CategoryFactory {
    Category create(Category.Type type, String name);
}