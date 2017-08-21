package ua.com.juja.magcraft.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.magcraft.sqlcmd.controller.Configuration;
import ua.com.juja.magcraft.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private Configuration configuration;
    private String connectionString;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        configuration = new Configuration();
        connectionString = "connect|" + configuration.GetDatabaseName() +
                "|" + configuration.GetDatabaseUserName() + "|" + configuration.GetDatabaseUserPassword();
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
                "\t- find|TableName\n" +
                "\t\t * show content of the table. Which name is TableName\n" +
                "\t- clear|TableName\n" +
                "\t\t * clear content of the table. Which name is TableName\n" +
                "\t- create|TableName|column1|value1|...|columnN|valueN\n" +
                "\t\t * create new row in the table. Which name is TableName\n" +
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
        in.add(connectionString);
        in.add("unsupported");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "command 'unsupported' does not exist\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add(connectionString);
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[users]\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        //given
        in.add(connectionString);
        in.add("clear|users");
        in.add("create|users|id|35|name|Andy|pass|realypass");
        in.add("create|users|id|36|name|Sandy|pass|ghdgashdfhsdf");
        in.add("find|users");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "Table 'users' successfully cleared\n" +
                "Get your command or 'help' for information:\n" +
                "A row: '{names: [id, name, pass], values: [35, Andy, realypass]}' was add to the table 'users'\n" +
                "Get your command or 'help' for information:\n" +
                "A row: '{names: [id, name, pass], values: [36, Sandy, ghdgashdfhsdf]}' was add to the table 'users'\n" +
                "Get your command or 'help' for information:\n" +
                "|id\t|name\t|pass\t|\n" +
                "|35\t|Andy\t|realypass\t|\n" +
                "|36\t|Sandy\t|ghdgashdfhsdf\t|\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add(connectionString);
        in.add("list");
        //тут используется подключение к другой базе данных которая называется "test" с фиксироваными значениями в таблице
        in.add("connect|test|" + configuration.GetDatabaseUserName() + "|" + configuration.GetDatabaseUserPassword());
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[users]\n" +
                "Get your command or 'help' for information:\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "[test]\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testConnectWithError() {
        //given
        in.add("connect|SQLCMD|");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "Operation failed: Wrong arguments, expected 4, separated by symbol '|' but was: 2\n" +
                "Try again.\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testFindWithErrorInCommand() {
        //given
        in.add(connectionString);
        in.add("find|");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "command find requires a parameter after '|' table name, like a find|tableName\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testClear() {
        //given
        in.add(connectionString);
        in.add("clear|users");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "Table 'users' successfully cleared\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testClearWithError() {
        //given
        in.add(connectionString);
        in.add("clear|");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "Operation failed: Wrong arguments, expected 2, separated by symbol '|' but was: 1\n" +
                "Try again.\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }

    @Test
    public void testCreateWithError() {
        //given
        in.add(connectionString);
        in.add("create|users|errorMessage");
        in.add("exit");
        //when
        Main.main(new String [0]);
        //then
        assertEquals("Welcome back!\n" +
                "If you're going to connect to the database.\n" +
                "Enter 'connect|dataBase|userName|password' please!\n" +
                "You've successfully connected!\n" +
                "Get your command or 'help' for information:\n" +
                "Operation failed: There have to even numbers of arguments 'create|TableName|column1|value1|...|columnN|valueN' but was: 'create|users|errorMessage'\n" +
                "Try again.\n" +
                "Get your command or 'help' for information:\n" +
                "Good luck\n", getData());
    }
}
