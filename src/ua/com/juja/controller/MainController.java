package ua.com.juja.controller;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

public class MainController {

    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDb();
    }

    private void connectToDb() {
        view.write("Welcome back!");
        while (true) {
            try {
                view.write("If you're going to connect to the database.");
                view.write("Enter " + "'" + "dataBase|userName|password" + "'" + " please!");
                String loginString = view.read();
                String[] data = loginString.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Wrong arguments, expected 3, separated by symbol " + "'|'" + " but was " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("You've succesfully connected!");
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
