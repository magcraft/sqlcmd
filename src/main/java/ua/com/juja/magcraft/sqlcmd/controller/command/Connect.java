package ua.com.juja.magcraft.sqlcmd.controller.command;

import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Connect implements Command {

    private static String COMMAND_SAMPLE = "connect|DatabaseName|UserName|Password";
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
    }
}
