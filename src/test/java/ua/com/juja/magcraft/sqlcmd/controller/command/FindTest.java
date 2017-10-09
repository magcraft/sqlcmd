package ua.com.juja.magcraft.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.magcraft.sqlcmd.model.DataSet;
import ua.com.juja.magcraft.sqlcmd.model.DatabaseManager;
import ua.com.juja.magcraft.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class FindTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Find(manager, view);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testFindWithData() {
        //given
        setupTableColumns("users", "id", "name", "pass");
        DataSet userOne = putUser(1, "Victor", "my_pass");
        DataSet userTwo = putUser(2, "Eva", "-=-=-=-=");
        DataSet[] data = new DataSet[] {userOne, userTwo};
        Mockito.when(manager.getTableData("users")).thenReturn(data);
        //when
        command.process("find|users");
        //then
        shouldPrint("[|id\t|name\t|pass\t|," +
                            " |1\t|Victor\t|my_pass\t|," +
                            " |2\t|Eva\t|-=-=-=-=\t|]");
    }

    private void setupTableColumns(String tableName, String... columns) {
        Mockito.when(manager.getTableColumns(tableName)).thenReturn(new LinkedHashSet<String>(Arrays.asList(columns)));
    }

    private DataSet putUser(int id, String name, String pass) {
        DataSet ourUser = new DataSet();
        ourUser.put("id", id);
        ourUser.put("name", name);
        ourUser.put("pass", pass);
        return ourUser;
    }

    @Test
    public void testFindWithoutData() {
        //given
        setupTableColumns("users", "id", "name", "pass");
        Mockito.when(manager.getTableData("users")).thenReturn(new DataSet[0]);
        //when
        command.process("find|users");
        //then
        shouldPrint("[|id\t|name\t|pass\t|]");
    }

    @Test
    public void testFindWithoutDataWithOneColumn() {
        //given
        setupTableColumns("test", "id");
        Mockito.when(manager.getTableData("test")).thenReturn(new DataSet[0]);
        //when
        command.process("find|test");
        //then
        shouldPrint("[|id\t|]");
    }

    @Test
    public void testFindWithDataOneColumn() {
        //given
        setupTableColumns("test", "id");
        DataSet userOne = new DataSet();
        userOne.put("id", 1);
        DataSet userTwo = new DataSet();
        userTwo.put("id", 2);
        DataSet[] data = new DataSet[] {userOne, userTwo};
        Mockito.when(manager.getTableData("test")).thenReturn(data);
        //when
        command.process("find|test");
        //then
        shouldPrint("[|id\t|," +
                " |1\t|," +
                " |2\t|]");
    }

    @Test
    public void testCanProcessFindString() {
        //when
        boolean canProcess = command.canProcess("find|users");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcessNotFindString() {
        //when
        boolean canProcess = command.canProcess("find");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanNotProcessQweString() {
        //when
        boolean canProcess = command.canProcess("qwe|users");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanNotProcessFindWithUnexpectedParametersString() {
        //when
        command.process("find|users|qwe");
        //then
        shouldPrint("[command find requires a parameter after '|' table name, like a find|tableName]");
//        assertFalse(canProcess);
    }
}
