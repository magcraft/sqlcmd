package ua.com.juja.magcraft.sqlcmd.controller.command;

import ua.com.juja.magcraft.sqlcmd.model.DataSet;
import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Find implements Command {

    private DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            view.write("command find requires a parameter after '|' table name, like a find|tableName");
            return;
        }
        String tableName = data[1];
        if (tableName != null) {
            DataSet[] tableContents = manager.getTableData(tableName);
            String[] tableColumns = manager.getTableColumns(tableName);
            printHeader(tableColumns);
            printTable(tableContents);
        }
    }

    private void printTable(DataSet[] tableContents) {
        for (DataSet row : tableContents) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value : values) {
            result += value.toString() + "\t|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColumns) {
        String result = "|";
        for (String name : tableColumns) {
            result += name + "\t|";
        }
        view.write(result);
    }

}