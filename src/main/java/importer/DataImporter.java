package importer;

import domain.Operation;
import service.OperationService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class DataImporter {
    protected final OperationService operationService;

    public DataImporter(OperationService operationService) {
        this.operationService = operationService;
    }

    public final void importData(String filePath) throws IOException {
        String content = readFile(filePath);
        List<Operation> operations = parse(content);
        for (Operation op : operations) {
            operationService.createOperation(
                    op.getType(), op.getBankAccountId(), op.getAmount(),
                    op.getDate(), op.getDescription(), op.getCategoryId()
            );
        }
    }

    protected abstract List<Operation> parse(String content) throws IOException;

    private String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }
}