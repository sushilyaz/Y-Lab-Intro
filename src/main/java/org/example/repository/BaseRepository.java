package org.example.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.config.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseRepository {
    protected static Connection connection;

    public static void initializeConnection() {
        try {
            connection = DatabaseConnection.getConnection();
            runLiquibaseMigrations();
            System.out.println("Migration is completed successfully");
        } catch (SQLException | LiquibaseException | IOException e) {
            System.out.println("Failed migration or connection with: " + e.getMessage());
        }

    }

    private static void runLiquibaseMigrations() throws SQLException, LiquibaseException {
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        System.out.println("Migration is completed successfully");
    }
}
