package ua.com.juja;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by magcraft on 19/05/2017.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //connect to PostgreSQL
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://10.211.55.6:5432/SQLCMD", "postgres",
                    "buh1762");
        //add a row
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO public.users (name, pass)"+
                "VALUES ('from java code', 'zxc')");

        connection.close();
    }
}
