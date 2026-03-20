package domain;

import java.time.LocalDate;
import java.util.Objects;

public class Operation {
    private final String id;
    private final Category.Type type;
    private final String bankAccountId;
    private final double amount;
    private final LocalDate date;
    private final String description;
    private final String categoryId;

    public Operation(String id, Category.Type type, String bankAccountId, double amount,
                     LocalDate date, String description, String categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public String getId() { return id; }
    public Category.Type getType() { return type; }
    public String getBankAccountId() { return bankAccountId; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategoryId() { return categoryId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}