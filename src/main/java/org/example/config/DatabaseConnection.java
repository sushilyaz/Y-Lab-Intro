package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection getConnection() throws IOException, SQLException {
        Properties properties = loadProperties();
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUsername = properties.getProperty("jdbc.username");
        String jdbcPassword = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
        }
        return properties;
    }
}
