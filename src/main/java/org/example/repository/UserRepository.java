package org.example.repository;

import org.example.model.Role;
import org.example.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Добавил многопоточный вариант
 */
public class UserRepository extends BaseRepository{
    private static volatile UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Сохранение пользователя в бд (при регистрации). Также добавил сиквенс
     */
    public void save(User user) {
        String sql = "INSERT INTO mainschema.users (id, username, password, role) VALUES (nextval('mainschema.seq_user'),?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRoleAsString());
            stmt.executeUpdate();
            connection.commit();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
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
        String sql = "SELECT * FROM mainschema.users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                User user = getUser(resultSet);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException  e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return Optional.empty();
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        Role modRole = Role.valueOf(role);
        return new User(id, username, password, modRole);
    }


}
