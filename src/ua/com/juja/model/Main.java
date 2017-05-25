package ua.com.juja.model;

import java.sql.*;
import java.util.Random;

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

        //Insert add a row
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO public.users (name, pass)"+
                "VALUES ('from java code', 'zxc')");
        stmt.close();


        //DELETE
        stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public.users WHERE id > 12");
        stmt.close();

        //update
        PreparedStatement ps = connection.prepareStatement("UPDATE public.users SET pass = ? WHERE id > 2");
        String pass = "@_" + new Random().nextInt();
        ps.setString(1, pass);
        ps.executeUpdate();
        ps.close();

        //SELECT
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM public.users WHERE id > 3");
        while (rs.next()) {
            System.out.println("id: " + rs.getString("id"));
            System.out.println("name: " + rs.getString("name"));
            System.out.println("pass: " + rs.getString("pass"));
            System.out.println("========================================");
        }
        rs.close();
        stmt.close();

        connection.close();
    }
}
