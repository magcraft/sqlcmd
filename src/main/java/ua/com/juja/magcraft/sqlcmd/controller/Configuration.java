package ua.com.juja.magcraft.sqlcmd.controller;

import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private Properties properties;
    public final String fileName = "config/sqlcmd.properties";

    public Configuration() {
        try {
            properties = new Properties();
            InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(is);
            is.close();
        } catch (Exception e) {
            System.out.println("Config was loading with error: " + fileName);
            e.printStackTrace();
        }
    }

    public String GetServerName() {
        return properties.getProperty("database.server.name");
    }

    public String GetDatabaseName() {
        return properties.getProperty("database.name");
    }

    public String GetDatabasePort() {
        return properties.getProperty("database.port");
    }

    public String GetDatabaseDriver() {
        return properties.getProperty("database.driver");
    }

    public String GetDatabaseUserName() {
        return properties.getProperty("database.user.name");
    }

    public String GetDatabaseUserPassword() {
        return properties.getProperty("database.user.password");
    }

    public String GetConnectionLogLevel() {
        return properties.getProperty("connection.log.level");
    }

}
