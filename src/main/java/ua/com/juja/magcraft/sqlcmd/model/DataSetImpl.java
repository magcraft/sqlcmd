package ua.com.juja.magcraft.sqlcmd.model;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap<String, Object>();

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public List<Object> getValues() {
        return new ArrayList<Object>(data.values());
    }

    @Override
    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public Object get(String name) {
        return data.get(name);
    }

    @Override
    public void updateFrom(DataSet input) {
        Set<String> columns = input.getNames();
        for (String name : columns) {
            Object data = input.get(name);
            put(name, data);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "names: " + getNames().toString() + ", " +
                "values: " + getValues().toString() +
        "}";
    }
}