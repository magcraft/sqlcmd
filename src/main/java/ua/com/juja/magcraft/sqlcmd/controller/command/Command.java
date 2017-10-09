package ua.com.juja.magcraft.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);

    //TODO создать методы для вывода формата комманды и описания
//    String format();

//    String description();
}
