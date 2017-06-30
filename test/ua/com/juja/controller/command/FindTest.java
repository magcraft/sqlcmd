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

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
    }

    @Test
    public void testFindWithData() {
        //given
        Command command = new Find(manager, view);
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
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[|id\t|name\t|pass\t|," +
                                " |1\t|Victor\t|my_pass\t|," +
                                " |2\t|Eva\t|-=-=-=-=\t|]", captor.getAllValues().toString());

    }

    @Test
    public void testFindWithoutData() {
        //given
        Command command = new Find(manager, view);
        Mockito.when(manager.getTableColumns("users")).thenReturn(new String[] {"id", "name", "pass"});

        DataSet[] data = new DataSet[0];
        Mockito.when(manager.getTableData("users")).thenReturn(data);

        //when
        command.process("find|users");

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[|id\t|name\t|pass\t|]", captor.getAllValues().toString());

    }

    @Test
    public void testCanProcessFindString() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("find|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcessNotFindString() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("finsdddc");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanNotProcessQweString() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("qwe|users");

        //then
        assertFalse(canProcess);
    }
}
