package org.example.repository;

import org.example.config.MyConnectionPool;
import org.example.dto.UserInfoDTO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminRepositoryImpl implements AdminRepository {


    /**
     * Добавление нового типа счетчика. Добавление осуществляется в пользователя админ. Он является неким "центром"
     */
    public void addNewType(String newKey) {
        String sql = "INSERT INTO mainschema.counter_reading (id, user_id, date, type, value) VALUES (nextval('mainschema.seq_cr'),1,'2000-01-01',?,1)";
        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newKey);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    /**
     * Поиск всех данных всех пользователей
     */
    public List<UserInfoDTO> findUsersAndCR() {
        String sql = "SELECT mainschema.users.username, mainschema.counter_reading.date, mainschema.counter_reading.type, mainschema.counter_reading.value\n" +
                "FROM mainschema.users\n" +
                "JOIN mainschema.counter_reading ON mainschema.users.id = mainschema.counter_reading.user_id\n" +
                "ORDER BY mainschema.users.username, mainschema.counter_reading.date";

        try (Connection connection = MyConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            List<UserInfoDTO> results = new ArrayList<>();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String type = resultSet.getString("type");
                Double value = resultSet.getDouble("value");
                UserInfoDTO userInfoDTO = new UserInfoDTO(username, date, type, value);
                results.add(userInfoDTO);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
