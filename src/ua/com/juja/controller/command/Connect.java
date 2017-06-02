package ua.com.juja.controller.command;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

public class Connect implements Command {

    private final String COMMAND_SAMPLE = "connect|DatabaseName|UserName|Password";
    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        try {
            int count = COMMAND_SAMPLE.split("\\|").length;
            String[] data = command.split("[|]");
            if (data.length != count) {
                throw new IllegalArgumentException(String.format("Wrong arguments, " +
                        "expected %s, separated by symbol '|' but was: %s", count, data.length));
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];
            manager.connect(databaseName, userName, password);
            view.write("You've succesfully connected!");
        } catch (Exception e) {
            printError(e);
        }
    }

    private void printError(Exception e) {
        String message = /*e.getClass().getSimpleName() + ": " +*/ e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + /*cause.getClass().getSimpleName() + ": " +*/ e.getCause().getMessage();
        }
        view.write("Connection failed: " + message);
        view.write("Try again.");
    }
}
