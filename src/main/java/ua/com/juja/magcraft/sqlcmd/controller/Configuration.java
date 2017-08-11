package ua.com.juja.magcraft.sqlcmd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private Properties properties;
    public final String fileName = "config/sqlcmd.properties";

    public Configuration() {
        FileInputStream fileInput = null;
        properties = new Properties();
        File file = new File(fileName);
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
        } catch (Exception e) {
            System.out.println("Config was loading with error: " + file.getAbsolutePath());
            e.printStackTrace();
        } finally {
            if (fileInput != null) try {
                fileInput.close();
            } catch (IOException e) {
                //do nothing
            }
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
