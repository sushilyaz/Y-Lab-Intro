package org.example.repository;

import org.example.dto.UserInfoDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends BaseRepository{
    /**
     * Для синглтона (добавил многопоточный вариант, как сказал ментор)
     */
    private static volatile AdminRepository instance;

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
            synchronized (AdminRepository.class) {
                if (instance == null) {
                    instance = new AdminRepository();
                }
            }
        }
        return instance;
    }

    /**
     *  Добавление нового типа счетчика. Добавление осуществляется в пользователя админ. Он является неким "центром"
     */
    public void addNewType(String newKey) {
        String sql = "INSERT INTO mainschema.counter_reading (id, user_id, year, month, type, value) VALUES (nextval('mainschema.seq_cr'),1,2000,1,?,1)";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newKey);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }
    /**
     *  Поиск всех данных всех пользователей
     */
    public List<UserInfoDTO> findUsersAndCR() {
        String sql = "SELECT mainschema.users.username, mainschema.counter_reading.year, mainschema.counter_reading.month, mainschema.counter_reading.type, mainschema.counter_reading.value\n" +
                "FROM mainschema.users\n" +
                "JOIN mainschema.counter_reading ON mainschema.users.id = mainschema.counter_reading.user_id\n" +
                "ORDER BY mainschema.users.username, mainschema.counter_reading.year, mainschema.counter_reading.month";

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
