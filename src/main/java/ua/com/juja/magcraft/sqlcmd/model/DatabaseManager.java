package ua.com.juja.magcraft.sqlcmd.model;

public interface DatabaseManager {

    DataSet[] getTableData(String tableName);

    String[] getTableNames();

    void connect(String databaseName, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet input);

    String[] getTableColumns(String tableName);

    boolean isConnected();
}
