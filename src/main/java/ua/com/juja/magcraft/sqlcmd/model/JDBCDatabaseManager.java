package ua.com.juja.magcraft.sqlcmd.model;

import ua.com.juja.magcraft.sqlcmd.controller.Configuration;
import java.sql.*;
import java.util.Arrays;

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
    public DataSet[] getTableData(String tableName) {
        int size = getSize(tableName);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName))
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) {
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
    public String[] getTableNames() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables " +
                     "WHERE table_schema = 'public' AND table_type = 'BASE TABLE'"))
        {
            int countTables = getCountTables();
            String[] tables = new String[countTables];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
          e.printStackTrace();
          return new String[0];
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
    public String[] getTableColumns(String tableName) {
        int countColumns = getCountColumns(tableName);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"
                     + tableName + "' ORDER BY ordinal_position"))
        {
            String[] tables = new String[countColumns];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
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