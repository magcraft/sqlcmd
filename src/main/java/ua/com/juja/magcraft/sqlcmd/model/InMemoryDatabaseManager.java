package ua.com.juja.magcraft.sqlcmd.model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap<>();

    @Override
    public List<DataSet> getTableData(String tableName) {
        return get(tableName);
    }

    @Override
    public int getSize(String tableName) {
        return get(tableName).size();
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        //do nothing because we use a DataSet
    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }

    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }

    @Override
    public void create(String tableName, DataSet input) {
        get(tableName).add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet input) {
        for (DataSet dataSet : get(tableName)) {
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
