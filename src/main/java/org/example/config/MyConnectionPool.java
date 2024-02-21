package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Написал свой коннекшен пул, как советовали менторы. В репозиториях коннекшен всегда возвращал
 */
@Component
public class MyConnectionPool {
    private YamlReader yamlConfig;

    @Autowired
    public MyConnectionPool(YamlReader yamlConfig) {
        this.yamlConfig = yamlConfig;
    }

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
        Class.forName("org.postgresql.Driver");
        String jdbcUrl = yamlConfig.getUrl();
        String jdbcUsername = yamlConfig.getUsername();
        String jdbcPassword = yamlConfig.getPassword();
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
}
