package ua.com.juja.controller.command;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

public class IsConnected implements Command {

    private DatabaseManager manager;
    private View view;

    public IsConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("You can not use comand: '%s' while you are not connected to the data base. For connection use command: 'connect'. ", command));
    }
}
