package ua.com.juja.magcraft.sqlcmd.model;

import java.util.Set;

public interface DatabaseManager {

    DataSet[] getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String databaseName, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet input);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
