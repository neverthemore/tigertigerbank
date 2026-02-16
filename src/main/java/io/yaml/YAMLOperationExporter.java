package io.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Operation;
import io.DataExporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YAMLOperationExporter implements DataExporter<Operation> {
    private final ObjectMapper mapper;

    public YAMLOperationExporter() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void export(List<Operation> data, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), data);
    }
}