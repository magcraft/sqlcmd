package ua.com.juja.magcraft.sqlcmd.view;

public class FakeView implements View {

    private String messages = "";
    private String input = null;
    private String goNewLine = System.getProperty("line.separator");


    @Override
    public void write(String message) {
        messages += message + goNewLine;
    }

    @Override
    public String read() {
        if (this.input == null) {
            throw new IllegalThreadStateException("Initialize read() please!");
        }
        String result = this.input;
        this.input = null;
        return result;
    }

    public void addRead(String input) {
        this.input = input;
    }

    public String getContent() {
        return messages;
    }
}
