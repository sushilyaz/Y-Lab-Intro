package org.example.repository;

import org.example.model.Role;
import org.example.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс репозитория пользователя. Данные хранятся в листе. Реализован паттерн синглтон, вроде как множественного
 * доступа к этому приложению не планируется, поэтому реализовал не потокобезопасный вариант
 */
public class UserRepository extends BaseRepository{
    private static UserRepository instance;
    private List<User> users = new ArrayList<>();

    private UserRepository() {
        initializeUsers();
    }

    /**
     * При инициализации класса также добавляется "Администратор в бд"
     */
    private void initializeUsers() {}

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    /**
     * Реализовал для отката репозитория (для тестирования)
     */
    public static void reset() {
        instance = null;
    }
    /**
     * Получение всех пользователей (Для админа и для теста)
     */
    public List<User> getUsers() {
        String sql = "SELECT * FROM users ORDER BY id";
        try (var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var results = new ArrayList<User>();
            while (resultSet.next()) {
                var user = getUser(resultSet);
                results.add(user);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Сохранение пользователя в бд (при регистрации)
     */
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?,?,?)";
        try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRoleAsString());
            stmt.executeUpdate();

            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Trouble with generate id");
            }
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    /**
     * Поиск по username
     * @return Optional<User>
     */
    public Optional<User> findByUsername(String username) {
        var sql = "SELECT * FROM users WHERE name = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var user = getUser(resultSet);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return Optional.empty();
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("id");
        var username = resultSet.getString("username");
        var password = resultSet.getString("password");
        var role = Role.valueOf(resultSet.getString("role"));
        return new User(id, username, password, role);
    }


}
