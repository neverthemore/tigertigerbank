package command;

import facade.ImportExportFacade;

public class ImportCommand implements Command {
    private final ImportExportFacade importExportFacade;
    private final String format;

    public ImportCommand(ImportExportFacade importExportFacade, String format) {
        this.importExportFacade = importExportFacade;
        this.format = format;
    }

    @Override
    public void execute() {
        try {
            switch (format) {
                case "json":
                    importExportFacade.importJson("operations.json");
                    break;
                case "yaml":
                    importExportFacade.importYaml("operations.yaml");
                    break;
                case "csv":
                    importExportFacade.importCsv("operations.csv");
                    break;
                default:
                    System.out.println("Неизвестный формат");
                    return;
            }
            System.out.println("Импорт из " + format.toUpperCase() + " выполнен");
        } catch (Exception e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
    }
}