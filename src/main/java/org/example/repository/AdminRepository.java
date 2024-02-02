package org.example.repository;

import org.example.dto.UserInfoDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends BaseRepository{
    /**
     * Для синглтона
     */
    private static AdminRepository instance;

    /**
     * приватный конструктор для синглтона
     */
    private AdminRepository() {
    }

    /**
     * Для синглтона
     */
    public static AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    public void addNewType(String newKey) {
        String sql = "INSERT INTO mainschema.counter_reading (user_id, year, month, type, value) VALUES (1,2000,1,?,1)";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newKey);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    public List<UserInfoDTO> findUsersAndCR() {
        String sql = "SELECT mainschema.users.username, mainschema.counter_reading.year, mainschema.counter_reading.month, mainschema.counter_reading.type, mainschema.counter_reading.value\n" +
                "FROM mainschema.users\n" +
                "JOIN mainschema.counter_reading ON mainschema.users.id = mainschema.counter_reading.user_id\n" +
                "ORDER BY mainschema.users.username";

        try (var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<UserInfoDTO>();
            while (resultSet.next()) {
                var username = resultSet.getString("username");
                var year = resultSet.getInt("year");
                var month = resultSet.getInt("month");
                var type = resultSet.getString("type");
                var value = resultSet.getDouble("value");
                UserInfoDTO userInfoDTO = new UserInfoDTO(username, year, month, type, value);
                results.add(userInfoDTO);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
