package org.example.repository;

import liquibase.exception.LiquibaseException;
import org.example.config.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.example.config.DatabaseConfig.runLiquibaseMigrations;

public class BaseRepository {
    public static Connection connection;

    public static void initializeConnection() {
        try {
            connection = DatabaseConfig.getConnection();
            runLiquibaseMigrations(connection);
        } catch (SQLException | LiquibaseException | IOException e) {
            System.out.println("Failed migration or connection with: " + e.getMessage());
        }
    }
}
