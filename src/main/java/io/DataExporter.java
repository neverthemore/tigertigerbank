package io;

import java.io.IOException;
import java.util.List;

public interface DataExporter<T> {
    void export(List<T> data, String filePath) throws IOException;
}