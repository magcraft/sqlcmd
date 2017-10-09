package ua.com.juja.magcraft.sqlcmd.model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "test, users";
    private List<DataSet> data = new LinkedList<DataSet>();

    @Override
    public List<DataSet> getTableData(String tableName) {
        return data;
    }

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<String>(Arrays.asList(TABLE_NAME));
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        //do nothing because we use a DataSet
    }

    @Override
    public void clear(String tableName) {
        data.clear();
    }

    @Override
    public void create(String tableName, DataSet input) {
        data.add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet input) {
        for (DataSet dataSet : data) {
            if (dataSet.get("id").equals(id)) {
                dataSet.updateFrom(input);
            }
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("id", "name", "pass"));
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
