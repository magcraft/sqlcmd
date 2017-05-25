package ua.com.juja.model;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by magcraft on 19/05/2017.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    @Override
    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName);
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
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
            rsCount.next();
            int size = rsCount.getInt(1);
            rsCount.close();
            return size;
        } catch (SQLException e) {
            System.out.println("Warning problems with getSize " + tableName + " method!");
            e.printStackTrace();
            return 0;
        }
    }

    private int getCountTables() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rsCountTables = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';");
            rsCountTables.next();
            int countTables = rsCountTables.getInt(1);
            rsCountTables.close();
            stmt.close();
            return countTables;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String[] getTableNames() {
        try {
            int countTables = getCountTables();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'");
            String[] tables = new String[countTables];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            rs.close();
            stmt.close();
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
//            System.out.println("Please add JDBC jar driver to project!");
//            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://10.211.55.6:5432/" + databaseName, userName,
                    password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(
                    String.format("Can't get connection to the database: %s, user: %s",
                            databaseName, userName),
                    e);
        }
    }

    @Override
    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM public." + tableName);
//            stmt.executeUpdate("DELETE FROM public.users WHERE id > 12");
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try {
            Statement stmt = connection.createStatement();
            String fieldNames = "";
            String values = "";
            fieldNames = getNamesFromated(input, "%s,");
            values = getValuesFormated(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + fieldNames + ")"+
                    "VALUES (" + values + ")");
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Create error!");
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSet input) {
        try {
            String updateCondition = getNamesFromated(input, "%s = ?,");

//          PreparedStatement ps = connection.prepareStatement("UPDATE public.users SET pass = ? WHERE id > 2");
            PreparedStatement stmt = connection.prepareStatement("UPDATE public." + tableName + " SET " + updateCondition +
                    " WHERE id = ?");
            int index = 1;
            for (Object value : input.getValues()) {
                stmt.setObject(index++, value);
            }
            stmt.setInt(index, id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Update error!");
            e.printStackTrace();
        }
    }

    private String getNamesFromated(DataSet input, String format) {
        String updateCondition = "";
        for (String name : input.getNames()) {
            updateCondition += String.format(format, name);
        }
        updateCondition = updateCondition.substring(0, updateCondition.length() - 1);
        return updateCondition;
    }

    private String getValuesFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }
}