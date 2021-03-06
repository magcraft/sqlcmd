package ua.com.juja.magcraft.sqlcmd.controller.command;

import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Clear implements Command {

    private static String COMMAND_SAMPLE = "clear|TableName";
    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear");
    }

    @Override
    public void process(String command) {
        int count = COMMAND_SAMPLE.split("\\|").length;
        String[] data = command.split("[|]");
        if (data.length != count) {
            throw new IllegalArgumentException(String.format("Wrong arguments, " +
                    "expected %s, separated by symbol '|' but was: %s", count, data.length));
        }
        String tableName = data[1];
        manager.clear(tableName);
        view.write(String.format("Table '%s' successfully cleared", tableName));
    }
}
