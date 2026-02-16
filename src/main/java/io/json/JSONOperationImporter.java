package io.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Operation;
import io.DataImporter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONOperationImporter implements DataImporter<Operation> {
    private final ObjectMapper mapper;

    public JSONOperationImporter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Operation> importData(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<List<Operation>>() {});
    }
}