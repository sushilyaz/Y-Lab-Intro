package org.example.repository;

import liquibase.exception.LiquibaseException;
import org.example.config.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.example.config.DatabaseConfig.runLiquibaseMigrations;

/**
 * Создал BaseRepository, от которого наследуются все остальные репозитории. Я это сделал по следующим причинам:
 * 1) Соединение с базой данных устанавливается 1 раз при запуске приложения. Если бы не было BaseRepository, то пришлось
 * бы устанавливать соединение при каждом обращении к репозиториям, что нагружало бы СУБД
 * 2) Для тестирования. Удобно тестировать приложение. Просто отправил соединение, которое установил в тестконтейнере в
 * BaseRepository. Поэтому connection имеет тип public, а не protected.
 */
public class BaseRepository {
    public static Connection connection;

    public static void initializeConnection() {
        try {
            connection = DatabaseConfig.getConnection();
            // Вызов метода для выполнения скриптов миграции
            runLiquibaseMigrations(connection);
        } catch (SQLException | LiquibaseException | IOException e) {
            System.out.println("Failed migration or connection with: " + e.getMessage());
        }
    }
}
