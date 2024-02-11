package org.example.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.repository.BaseRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class DatabaseConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            try {
                Connection connection = DatabaseConfig.getConnection();
                runLiquibaseMigrations(connection);
                BaseRepository.setConnection(connection);
            } catch (SQLException | LiquibaseException | IOException e) {
                System.out.println("Failed migration or connection with: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

    }

    /**
     *  Установка соединения с БД
     */
    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = loadProperties();
        Class.forName("org.postgresql.Driver");
        String jdbcUrl = properties.getProperty("jdbc.url");
        String jdbcUsername = properties.getProperty("jdbc.username");
        String jdbcPassword = properties.getProperty("jdbc.password");
        Connection con = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        return con;
    }

    /**
     * Метод загрузки файла настрек БД
     */
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
        }
        return properties;
    }
    /**
     * Метод, выполняющий скрипты миграции. Все сущности создаются в отдельной схеме - mainschema.
     * Служебные таблицы создаются в отдельной от сущностей схеме - public. Все как по ТЗ
     */

    public static void runLiquibaseMigrations(Connection connection) throws SQLException, LiquibaseException {
        Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        System.out.println("Migration is completed successfully");
    }
}
