package ua.com.juja.magcraft.sqlcmd.controller;

import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.Console;
import ua.com.juja.magcraft.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}