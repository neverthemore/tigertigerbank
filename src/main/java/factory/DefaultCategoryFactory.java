package factory;

import domain.Category;
import java.util.UUID;

public class DefaultCategoryFactory implements CategoryFactory {
    @Override
    public Category create(Category.Type type, String name) {
        return new Category(UUID.randomUUID().toString(), type, name);
    }
}