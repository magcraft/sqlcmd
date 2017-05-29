package ua.com.juja.controller.command;

import ua.com.juja.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Command list:");

        view.write("\t- connect to the database.");
        view.write(String.format("\t\t * Enter command 'connect|dataBase|userName|password' please!"));

        view.write("\t- exit:");
        view.write("\t\t * close the application.");

        view.write("\t- find|Table Name");
        view.write("\t\t * show content of the table. Which name is Table Name");

        view.write("\t- list:");
        view.write("\t\t * if you need to get list of tables in the database.");

        view.write("\t- help");
        view.write("\t\t * for this information message.");
    }
}
