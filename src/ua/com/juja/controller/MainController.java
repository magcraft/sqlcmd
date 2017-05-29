package ua.com.juja.controller;

import ua.com.juja.controller.command.*;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new List(manager, view),
                new Find(manager, view),
                new Unsupported(view)
        };
    }

    public void run() {
        view.write("Welcome back!");
        view.write("If you're going to connect to the database.");
        view.write("Enter " + "'" + "connect|dataBase|userName|password" + "'" + " please!");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Get your command or 'help' for information:");
        }
    }
}
