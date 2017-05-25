package ua.com.juja.model;

import java.util.Arrays;

public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "users";
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        return Arrays.copyOf(data, freeIndex);
    }

    @Override
    public String[] getTableNames() {
        return new String[] {TABLE_NAME};
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
}
