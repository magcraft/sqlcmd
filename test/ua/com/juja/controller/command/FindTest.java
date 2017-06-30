package ua.com.juja.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        Mockito.when(manager.getTableColumns("users")).thenReturn(new String[] {"id", "name", "pass"});
        DataSet userOne = new DataSet();
        userOne.put("id", 1);
        userOne.put("name", "Victor");
        userOne.put("pass", "my_pass");
        DataSet userTwo = new DataSet();
        userTwo.put("id", 2);
        userTwo.put("name", "Eva");
        userTwo.put("pass", "-=-=-=-=");
        DataSet[] data = new DataSet[] {userOne, userTwo};
        Mockito.when(manager.getTableData("users")).thenReturn(data);
        //when
        command.process("find|users");
        //then
        shouldPrint("[|id\t|name\t|pass\t|," +
                            " |1\t|Victor\t|my_pass\t|," +
                            " |2\t|Eva\t|-=-=-=-=\t|]");
    }

    @Test
    public void testFindWithoutData() {
        //given
        Mockito.when(manager.getTableColumns("users")).thenReturn(new String[] {"id", "name", "pass"});
        Mockito.when(manager.getTableData("users")).thenReturn(new DataSet[0]);
        //when
        command.process("find|users");
        //then
        shouldPrint("[|id\t|name\t|pass\t|]");
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
