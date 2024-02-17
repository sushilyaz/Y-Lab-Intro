package org.example.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

@Component
public class MyConnectionPool {
    private int MAX_CONNECTIONS = 10;
    private int connNum = 0;
    Stack<Connection> freePool = new Stack<>();
    Set<Connection> occupiedPool = new HashSet<>();
    public synchronized Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = null;
        if (isFull()) {
            throw new SQLException("The connection pool is full");
        }
        conn = getConnectionFromPool();
        if (conn == null) {
            conn = createNewConnectionPool();
        }
        conn = makeAvailable(conn);
        return conn;
    }

    public synchronized void returnConnection(Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException();
        }
        if (!occupiedPool.remove(conn)) {
            throw new NullPointerException("This connection is returned already or it isnt for this pool");
        }
        freePool.push(conn);
    }

    private synchronized boolean isFull() {
        return ((freePool.size() == 0) && connNum >= MAX_CONNECTIONS);
    }

    private Connection createNewConnectionPool() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = createNewConnection();
        connNum++;
        occupiedPool.add(conn);
        return conn;
    }

    private Connection createNewConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = null;
        Properties properties = loadProperties();
        Class.forName("org.postgresql.Driver");
        String jdbcUrl = properties.getProperty("url");
        String jdbcUsername = properties.getProperty("username");
        String jdbcPassword = properties.getProperty("password");
        conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        return conn;
    }

    private Connection getConnectionFromPool() {
        Connection conn = null;
        if (freePool.size() > 0) {
            conn = freePool.pop();
            occupiedPool.add(conn);
        }
        return conn;
    }

    private Connection makeAvailable(Connection conn) throws SQLException, IOException, ClassNotFoundException {
        occupiedPool.remove(conn);
        connNum--;
        conn.close();

        conn = createNewConnection();
        occupiedPool.add(conn);
        connNum++;
        return conn;
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Listener.class.getClassLoader().getResourceAsStream("application.yml")) {
            properties.load(input);
        }
        return properties;
    }
}
