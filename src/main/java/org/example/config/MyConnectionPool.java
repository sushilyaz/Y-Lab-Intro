package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

public class MyConnectionPool {
    private static final int MAX_CONNECTIONS = 10;
    private static LinkedList<Connection> connections = new LinkedList<>();


    public static void initializePool() throws IOException, ClassNotFoundException, SQLException {
        Properties properties = loadProperties();
        Class.forName("org.postgresql.Driver");
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUsername = properties.getProperty("jdbc.username");
        String jdbcPassword = properties.getProperty("jdbc.password");

        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            connections.add(DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword));
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Listener.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {
        if (connections.isEmpty()) {
            throw new SQLException("Connection pool is full");
        }
        return connections.poll();
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            connections.add(connection);
        }
    }

    public static void closePool() {
        for (Connection connection : connections) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
