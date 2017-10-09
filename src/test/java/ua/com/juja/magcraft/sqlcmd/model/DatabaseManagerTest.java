package ua.com.juja.magcraft.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.magcraft.sqlcmd.controller.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class DatabaseManagerTest {

    private DatabaseManager manager;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        manager = getDatabaseManager();
        manager.connect(configuration.GetDatabaseName(), configuration.GetDatabaseUserName(), configuration.GetDatabaseUserPassword());
    }

    public abstract DatabaseManager getDatabaseManager();

    @Test
    public void testGetAllTableNames() {
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[test, users]", tableNames.toString());
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
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[id, name, pass]", Arrays.toString(user.getNames()));
        assertEquals("[9, Pupkin, password]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clear("users");
        //when
        Set<String> columnNames = manager.getTableColumns("users");
        assertEquals("[id, name, pass]", columnNames.toString());
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

        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[id, name, pass]", Arrays.toString(user.getNames()));
        assertEquals("[9, Pupkina, password_@_1]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }
}
