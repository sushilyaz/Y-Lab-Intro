package org.example.repository;

import org.example.config.MyConnectionPool;
import org.example.model.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@Component
public class UserActionRepositoryImpl implements UserActionRepository {

    private MyConnectionPool myConnectionPool;

    @Autowired
    public UserActionRepositoryImpl(MyConnectionPool myConnectionPool) {
        this.myConnectionPool = myConnectionPool;
    }

    /**
     * Добавление в журнал
     */
    public void save(UserAction userAction) {
        String sql = "INSERT INTO mainschema.user_action (id, username, action, timestamp) VALUES (nextval('mainschema.seq_ua'),?,?,?)";
        try (Connection connection = myConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userAction.getUsername());
            stmt.setString(2, userAction.getAction());
            stmt.setTimestamp(3, Timestamp.valueOf(userAction.getTimestamp()));
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            myConnectionPool.returnConnection(connection);
            if (generatedKeys.next()) {
                userAction.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Trouble with generate id");
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    /**
     * Вывод логов (для администратора)
     */
    public List<UserAction> getUserActions() {
        String sql = "SELECT * FROM mainschema.user_action ORDER BY id";
        try (Connection connection = myConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            List<UserAction> results = new ArrayList<>();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String action = resultSet.getString("action");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                UserAction userAction = new UserAction(username, action, timestamp.toLocalDateTime());
                results.add(userAction);
            }
            myConnectionPool.returnConnection(connection);
            return results;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}


