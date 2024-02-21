package org.example.repository;

import org.example.config.MyConnectionPool;
import org.example.model.Role;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Component

public class UserRepositoryImpl implements UserRepository {

    private MyConnectionPool myConnectionPool;

    @Autowired
    public UserRepositoryImpl(MyConnectionPool myConnectionPool) {
        this.myConnectionPool = myConnectionPool;
    }

    public void save(User user) {
        String sql = "INSERT INTO mainschema.users (id, username, password, role) VALUES (nextval('mainschema.seq_user'),?,?,?)";
        try (Connection connection = myConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRoleAsString());
            stmt.executeUpdate();
//            connection.commit();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Trouble with generate id");
            }
            myConnectionPool.returnConnection(connection);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
        }
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM mainschema.users WHERE username = ?";
        try (Connection connection = myConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            myConnectionPool.returnConnection(connection);
            if (resultSet.next()) {
                User user = getUser(resultSet);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Trouble with statement: " + e.getMessage());
            return Optional.empty();
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        Role modRole = Role.valueOf(role);
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(modRole);
        return user;
    }


}
