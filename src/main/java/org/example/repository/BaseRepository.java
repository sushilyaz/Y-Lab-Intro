package org.example.repository;

import java.sql.Connection;

public class BaseRepository {
    protected static Connection connection;

    public static void setConnection(Connection connection) {
        BaseRepository.connection = connection;
    }
}
