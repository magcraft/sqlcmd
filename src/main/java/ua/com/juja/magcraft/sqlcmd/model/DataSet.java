package ua.com.juja.magcraft.sqlcmd.model;

public interface DataSet {
    void put(String name, Object value);

    Object[] getValues();

    String[] getNames();

    Object get(String name);

    void updateFrom(DataSet input);
}
