package domain;

import java.util.Objects;

public class Category {
    public enum Type { INCOME, EXPENSE }

    private final String id;
    private final Type type;
    private String name;

    public Category(String id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() { return id; }
    public Type getType() { return type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}