package ua.com.juja.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public abstract class DatabaseManagerTest {

    private DatabaseManager manager;

    @Before
    public void setup() {
        manager = getDatabaseManager();
        manager.connect("SQLCMD", "postgres", "buh1762");
    }

    public abstract DatabaseManager getDatabaseManager();

    @Test
    public void testGetAllTableNames() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[users]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("users");

        //when
        DataSet input = new DataSet();
        input.put("id", 9);
        input.put("name", "Pupkin");
        input.put("pass", "password");
        manager.create("users", input);

        //then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[id, name, pass]", Arrays.toString(user.getNames()));
        assertEquals("[9, Pupkin, password]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("users");

        DataSet input = new DataSet();
        input.put("id", 9);
        input.put("name", "Pupkin");
        input.put("pass", "password");
        manager.create("users", input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("id", 9);
        newValue.put("name", "Pupkina");
        newValue.put("pass", "password_@_1");
        manager.update("users",9, newValue);

        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[id, name, pass]", Arrays.toString(user.getNames()));
        assertEquals("[9, Pupkina, password_@_1]", Arrays.toString(user.getValues()));
    }
}
