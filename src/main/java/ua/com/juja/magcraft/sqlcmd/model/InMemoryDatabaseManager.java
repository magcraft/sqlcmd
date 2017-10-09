package ua.com.juja.magcraft.sqlcmd.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "test, users";
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        return Arrays.copyOf(data, freeIndex);
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
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet input) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].get("id").equals(id)) {
                data[index].updateFrom(input);
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
