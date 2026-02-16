package io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Operation;
import io.DataExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONOperationExporter implements DataExporter<Operation> {
    private final ObjectMapper mapper;

    public JSONOperationExporter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void export(List<Operation> data, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), data);
    }
}