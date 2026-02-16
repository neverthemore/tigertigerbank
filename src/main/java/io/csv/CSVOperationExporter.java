package io.csv;

import domain.Operation;
import io.DataExporter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVOperationExporter implements DataExporter<Operation> {
    @Override
    public void export(List<Operation> data, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,type,bankAccountId,amount,date,description,categoryId");
            for (Operation op : data) {
                writer.printf("%s,%s,%s,%.2f,%s,%s,%s%n",
                        op.getId(),
                        op.getType(),
                        op.getBankAccountId(),
                        op.getAmount(),
                        op.getDate(),
                        escapeCsv(op.getDescription()),
                        op.getCategoryId());
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}