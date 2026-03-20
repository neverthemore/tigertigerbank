package exporter;

import domain.Operation;
import java.io.IOException;
import java.util.List;

public interface DataExporter {
    void export(List<Operation> operations, String filePath) throws IOException;
}