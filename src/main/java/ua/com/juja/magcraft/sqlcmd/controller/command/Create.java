package ua.com.juja.magcraft.sqlcmd.controller.command;

import ua.com.juja.magcraft.sqlcmd.model.DataSet;
import ua.com.juja.magcraft.sqlcmd.model.DataSetImpl;
import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        int dataLength = data.length;
        String tableName = data[1];
        DataSet dataSet = new DataSetImpl();
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException(String.format("There have to even numbers of arguments 'create|TableName|column1|value1|...|columnN|valueN' but was: '%s'", command));
        }
        for (int i = 1; i < (dataLength / 2); i++) {
            String column = data[i * 2];
            String value = data[i * 2 + 1];
            dataSet.put(column, value);
        }
        manager.create(tableName, dataSet);
        view.write(String.format("A row: '%s' was add to the table '%s'", dataSet, tableName));
    }
}
