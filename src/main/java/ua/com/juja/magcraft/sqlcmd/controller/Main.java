package ua.com.juja.magcraft.sqlcmd.controller;

import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.Console;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        Configuration configuration = new Configuration();
        DatabaseManager manager = new JDBCDatabaseManager(configuration);
        MainController controller = new MainController(view, manager);
        controller.run();
    }
}