package org.example.repository;

import org.example.model.UserAction;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * Журнал для хранения всех действий
 * Действия фиксируются только пользователя, посчитал, что администратор не должен здесь светится
 * Также реализовал паттерн Синглтон, потому что используется в разных сервисах
 */
public class UserActionRepository extends BaseRepository {
    /**
     * для синглтона
     */
    private static UserActionRepository instance;

    /**
     * приватный конструктор для синглтона
     */
    private UserActionRepository() {}

    /**
     * Для синглтона
     */
    public static UserActionRepository getInstance() {
        if (instance == null) {
            instance = new UserActionRepository();
        }
        return instance;
    }

    /**
     * Добавление в журнал
     */
    public void save(UserAction userAction) {
        String sql = "INSERT INTO mainschema.user_action (id, username, action, timestamp) VALUES (nextval('mainschema.seq_ua'),?,?,?)";
        try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userAction.getUsername());
            stmt.setString(2, userAction.getAction());
            stmt.setTimestamp(3, Timestamp.valueOf(userAction.getTimestamp()));
            stmt.executeUpdate();
            connection.commit();
            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                userAction.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Trouble with generate id");
            }
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    /**
     * Вывод логов (для администратора)
     */
    public List<UserAction> getUserActions() {
        String sql = "SELECT * FROM mainschema.user_action ORDER BY id";
        try (var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<UserAction>();
            while (resultSet.next()) {
                var username = resultSet.getString("username");
                var action = resultSet.getString("action");
                var timestamp = resultSet.getTimestamp("timestamp");
                var userAction = new UserAction(username, action, timestamp.toLocalDateTime());
                results.add(userAction);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}


