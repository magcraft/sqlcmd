package ua.com.juja.magcraft.sqlcmd.controller.command;

import ua.com.juja.magcraft.sqlcmd.view.View;

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

        view.write("\t- find|TableName");
        view.write("\t\t * show content of the table. Which name is TableName");

        view.write("\t- clear|TableName");
        view.write("\t\t * clear content of the table. Which name is TableName");

        view.write("\t- create|TableName|column1|value1|...|columnN|valueN");
        view.write("\t\t * create new row in the table. Which name is TableName");

        view.write("\t- tables:");
        view.write("\t\t * if you need to get list of tables in the database.");

        view.write("\t- help");
        view.write("\t\t * for this information message.");
    }
}
