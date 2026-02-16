package io;

import java.io.IOException;
import java.util.List;

public interface DataImporter<T> {
    List<T> importData(String filePath) throws IOException;
}