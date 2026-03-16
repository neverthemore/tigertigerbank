package facade;

import domain.Operation;
import importer.DataImporter;
import exporter.DataExporter;
import java.io.IOException;
import java.util.List;

public class ImportExportFacade {
    private final DataImporter jsonImporter;
    private final DataImporter yamlImporter;
    private final DataImporter csvImporter;
    private final DataExporter jsonExporter;
    private final DataExporter yamlExporter;
    private final DataExporter csvExporter;

    public ImportExportFacade(DataImporter jsonImporter, DataImporter yamlImporter, DataImporter csvImporter,
                              DataExporter jsonExporter, DataExporter yamlExporter, DataExporter csvExporter) {
        this.jsonImporter = jsonImporter;
        this.yamlImporter = yamlImporter;
        this.csvImporter = csvImporter;
        this.jsonExporter = jsonExporter;
        this.yamlExporter = yamlExporter;
        this.csvExporter = csvExporter;
    }

    public void importJson(String path) throws IOException { jsonImporter.importData(path); }
    public void importYaml(String path) throws IOException { yamlImporter.importData(path); }
    public void importCsv(String path) throws IOException { csvImporter.importData(path); }

    public void exportJson(List<Operation> data, String path) throws IOException { jsonExporter.export(data, path); }
    public void exportYaml(List<Operation> data, String path) throws IOException { yamlExporter.export(data, path); }
    public void exportCsv(List<Operation> data, String path) throws IOException { csvExporter.export(data, path); }
}