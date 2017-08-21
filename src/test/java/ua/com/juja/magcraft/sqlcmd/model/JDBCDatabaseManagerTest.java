package ua.com.juja.magcraft.sqlcmd.model;

import ua.com.juja.magcraft.sqlcmd.controller.Configuration;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager(new Configuration());
    }
}
