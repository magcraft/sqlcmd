package ua.com.juja.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "Command list:\n" +
                "\t- connect to the database.\n" +
                "\t\t * Enter command 'connect|dataBase|userName|password' please!\n" +
                "\t- exit:\n" +
                "\t\t * close the application.\n" +
                "\t- find|Table Name\n" +
                "\t\t * show content of the table. Which name is Table Name\n" +
                "\t- list:\n" +
                "\t\t * if you need to get list of tables in the database.\n" +
                "\t- help\n" +
                "\t\t * for this information message.\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testExit() {
        //given
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You can not use comand: 'list' while you are not connected to the data base. For connection use command: 'connect'. \n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        //given
        in.add("find|users");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You can not use comand: 'find|users' while you are not connected to the data base. For connection use command: 'connect'. \n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        in.add("unsupported");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You can not use comand: 'unsupported' while you are not connected to the data base. For connection use command: 'connect'. \n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add("connect|SQLCMD|postgres|buh1762");
        in.add("unsupported");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've succesfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "command 'unsupported' does not exist\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add("connect|SQLCMD|postgres|buh1762");
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've succesfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[users]\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        //given
        in.add("connect|SQLCMD|postgres|buh1762");
        in.add("find|users");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've succesfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "|id|name|pass|\n" +
                "|35|Andy|mypassword|\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add("connect|SQLCMD|postgres|buh1762");
        in.add("list");
        in.add("connect|test|postgres|buh1762");
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've succesfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[users]\n" +
                "Get your command or 'help' for information:\n" +
                "You've succesfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[test]\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }
}
