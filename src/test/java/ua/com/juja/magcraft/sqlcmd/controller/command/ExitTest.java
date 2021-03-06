package ua.com.juja.magcraft.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.magcraft.sqlcmd.view.FakeView;


import static org.junit.Assert.*;

public class ExitTest {
    private FakeView view = new FakeView();
    private Command command;
    private String goNewLine = System.getProperty("line.separator");

    @Before
    public void setup() {
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        //when
        boolean canProcess = command.canProcess("exit");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcessQweString() {
        //when
        boolean canProcess = command.canProcess("qwe");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_throwsExitException() {
        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //then
            // throws ExitException
            assertEquals("Good luck" + goNewLine, view.getContent());
        }
    }

}
