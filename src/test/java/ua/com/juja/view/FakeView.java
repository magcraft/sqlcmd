package ua.com.juja.view;

public class FakeView implements View {

    private String messages = "";
    private String input = null;


    @Override
    public void write(String message) {
        messages += message + "\n";
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
