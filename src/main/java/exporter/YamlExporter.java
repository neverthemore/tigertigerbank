package exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Operation;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlExporter implements DataExporter {
    private final ObjectMapper mapper;

    public YamlExporter() {
        this.mapper = new ObjectMapper(new YAMLFactory()).registerModule(new JavaTimeModule());
    }

    @Override
    public void export(List<Operation> operations, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), operations);
    }
}