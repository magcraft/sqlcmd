package ua.com.juja.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        //given

        //when
        command.process("clear|users");
        //then
        Mockito.verify(manager).clear("users");
        Mockito.verify(view).write("Table 'users' sucsessfully cleared");
    }

    @Test
    public void testCanProcessClearString() {
        //when
        boolean canProcess = command.canProcess("clear|users");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcessQweString() {
        //when
        boolean canProcess = command.canProcess("qwe|users");
        //then
        assertFalse(canProcess);
    }

//    @Test
//    public void testCanNotProcessFindWithUnexpectedParametersString() {
//        //when
//        command.process("clear|users|qwe");
//
//        //then
//        Mockito.verify(view).write(new IllegalArgumentException("Wrong arguments, expected 2, separated by symbol '|' but was: 3").toString());
//    }

}
