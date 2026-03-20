package command;

public class TimedCommand implements Command {
    private final Command delegate;

    public TimedCommand(Command delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute() {
        long start = System.nanoTime();
        delegate.execute();
        long end = System.nanoTime();
        System.out.println("Время выполнения: " + (end - start) / 1_000_000 + " мс");
    }
}