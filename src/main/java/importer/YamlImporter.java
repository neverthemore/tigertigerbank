package importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Operation;
import service.OperationService;
import java.io.IOException;
import java.util.List;

public class YamlImporter extends DataImporter {
    private final ObjectMapper mapper;

    public YamlImporter(OperationService operationService) {
        super(operationService);
        this.mapper = new ObjectMapper(new YAMLFactory()).registerModule(new JavaTimeModule());
    }

    @Override
    protected List<Operation> parse(String content) throws IOException {
        return mapper.readValue(content, new TypeReference<List<Operation>>() {});
    }
}