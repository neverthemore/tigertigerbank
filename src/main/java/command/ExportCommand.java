package command;

import facade.ImportExportFacade;
import facade.OperationFacade;

public class ExportCommand implements Command {
    private final ImportExportFacade importExportFacade;
    private final OperationFacade operationFacade;
    private final String format; // "json", "yaml", "csv"

    public ExportCommand(ImportExportFacade importExportFacade, OperationFacade operationFacade, String format) {
        this.importExportFacade = importExportFacade;
        this.operationFacade = operationFacade;
        this.format = format;
    }

    @Override
    public void execute() {
        try {
            switch (format) {
                case "json":
                    importExportFacade.exportJson(operationFacade.getAllOperations(), "operations.json");
                    break;
                case "yaml":
                    importExportFacade.exportYaml(operationFacade.getAllOperations(), "operations.yaml");
                    break;
                case "csv":
                    importExportFacade.exportCsv(operationFacade.getAllOperations(), "operations.csv");
                    break;
                default:
                    System.out.println("Неизвестный формат");
                    return;
            }
            System.out.println("Экспорт в " + format.toUpperCase() + " выполнен");
        } catch (Exception e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}