package ua.com.juja.magcraft.sqlcmd.model;

import ua.com.juja.magcraft.sqlcmd.controller.Configuration;

import java.sql.*;
import java.util.*;

/**
 * Created by magcraft on 19/05/2017.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;
    private String driver;
    private String address;
    private String port;
    private String logLevel;

    public JDBCDatabaseManager(Configuration configuration) {
        this.driver = configuration.GetDatabaseDriver();
        this.address = configuration.GetServerName();
        this.port = configuration.GetDatabasePort();
        this.logLevel = configuration.GetConnectionLogLevel();
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        int size = getSize(tableName);
        List<DataSet> result = new ArrayList<>(size);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName))
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    //i+1 Because JDBC Counts start from  1
                    dataSet.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName))
        {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        } catch (SQLException e) {
            System.out.println("Warning problems with getSize " + tableName + " method!");
            e.printStackTrace();
            return 0;
        }
    }

    private int getCountTables() {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCountTables = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';"))
        {
            rsCountTables.next();
            int countTables = rsCountTables.getInt(1);
            return countTables;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Set<String> getTableNames() {
        int countTables = getCountTables();
        Set<String> tables = new LinkedHashSet<String>(countTables);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables " +
                     "WHERE table_schema = 'public' AND table_type = 'BASE TABLE'"))
        {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
          e.printStackTrace();
          return tables;
        }
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add JDBC jar driver to project!", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(driver + address + ":" + port + "/" +
                    databaseName +
                    logLevel,
                    userName,
                    password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(
                    String.format("Can't get connection to the server: '%s', database: '%s', user: '%s'",
                            address, databaseName, userName),
                    e);
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement()){
            stmt.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        String fieldNames = "";
        String values = "";
        try (Statement stmt = connection.createStatement()) {
            fieldNames = getNamesFormatted(input, "%s,");
            values = getValuesFormatted(input, "'%s',");
            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + fieldNames + ")"+
                    "VALUES (" + values + ")");
        } catch (SQLException e) {
            System.out.println("Create error!");
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSet input) {
        String updateCondition = getNamesFormatted(input, "%s = ?,");
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE public." + tableName + " SET " +
                updateCondition + " WHERE id = ?")) {
            int index = 1;
            for (Object value : input.getValues()) {
                stmt.setObject(index++, value);
            }
            stmt.setInt(index, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update error!");
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        int countColumns = getCountColumns(tableName);
        Set<String> tables = new LinkedHashSet<String>(countColumns);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"
                     + tableName + "' ORDER BY ordinal_position"))
        {
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private int getCountColumns(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rscc = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName + "'"))
        {
            rscc.next();
            int countColumns = rscc.getInt(1);
            return countColumns;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getNamesFormatted(DataSet input, String format) {
        String updateCondition = "";
        for (String name : input.getNames()) {
            updateCondition += String.format(format, name);
        }
        updateCondition = updateCondition.substring(0, updateCondition.length() - 1);
        return updateCondition;
    }

    private String getValuesFormatted(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }
}