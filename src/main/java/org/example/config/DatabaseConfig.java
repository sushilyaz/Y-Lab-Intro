package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    public static Connection getConnection() throws IOException, SQLException {
        Properties properties = loadProperties();
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUsername = properties.getProperty("jdbc.username");
        String jdbcPassword = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
        }
        return properties;
    }
    public static void runLiquibaseMigrations(Connection connection) throws SQLException, LiquibaseException {
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        System.out.println("Migration is completed successfully");
    }
}
