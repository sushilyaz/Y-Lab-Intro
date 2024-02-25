package org.example.audit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.audit.model.UserAction;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Журнал для хранения всех действий
 * Действия фиксируются только пользователя, посчитал, что администратор не должен здесь светится
 * Также реализовал паттерн Синглтон, потому что используется в разных сервисах
 */
public class UserActionRepositoryImpl implements UserActionRepository {

    private Connection connection;

    /**
     * Добавление в журнал
     */
    public void save(UserAction userAction) {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/suhoi", "suhoi", "qwerty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Audit starter connection to db success");
        String sql = "INSERT INTO mainschema.user_action (id, username, action, timestamp) VALUES (nextval('mainschema.seq_ua'),?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userAction.getUsername());
            stmt.setString(2, userAction.getAction());
            stmt.setTimestamp(3, Timestamp.valueOf(userAction.getTimestamp()));
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                userAction.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Trouble with generate id");
            }
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }
}
