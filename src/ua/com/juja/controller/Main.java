package ua.com.juja.controller;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.model.JDBCDatabaseManager;
import ua.com.juja.view.Console;
import ua.com.juja.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}