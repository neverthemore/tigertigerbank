package io.csv;

import domain.Category;
import domain.Operation;
import io.DataImporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVOperationImporter implements DataImporter<Operation> {
    @Override
    public List<Operation> importData(String filePath) throws IOException {
        List<Operation> operations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine(); // пропускаем заголовок
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length < 7) continue;
                String id = fields[0];
                Category.Type type = Category.Type.valueOf(fields[1]);
                String bankAccountId = fields[2];
                double amount = Double.parseDouble(fields[3]);
                LocalDate date = LocalDate.parse(fields[4]);
                String description = fields[5];
                String categoryId = fields[6];
                operations.add(new Operation(id, type, bankAccountId, amount, date, description, categoryId));
            }
        }
        return operations;
    }

    private String[] parseCsvLine(String line) {
        return line.split(",");
    }
}