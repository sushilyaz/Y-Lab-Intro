package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseMigration {
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
