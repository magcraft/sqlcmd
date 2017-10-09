package ua.com.juja.magcraft.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    List<DataSet> getTableData(String tableName);

    int getSize(String tableName);

    Set<String> getTableNames();

    void connect(String databaseName, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet input);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
