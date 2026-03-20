package importer;

import domain.Category;
import domain.Operation;
import service.OperationService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter extends DataImporter {
    public CsvImporter(OperationService operationService) {
        super(operationService);
    }

    @Override
    protected List<Operation> parse(String content) throws IOException {
        List<Operation> result = new ArrayList<>();
        String[] lines = content.split("\n");
        // пропускаем заголовок
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 7) continue;
            // id,type,bankAccountId,amount,date,description,categoryId
            String id = parts[0];
            Category.Type type = Category.Type.valueOf(parts[1]);
            String bankId = parts[2];
            double amount = Double.parseDouble(parts[3]);
            LocalDate date = LocalDate.parse(parts[4]);
            String desc = parts[5];
            String catId = parts[6];
            result.add(new Operation(id, type, bankId, amount, date, desc, catId));
        }
        return result;
    }
}