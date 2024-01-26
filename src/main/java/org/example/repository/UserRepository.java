package org.example.repository;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static UserRepository instance;
    private List<User> users = new ArrayList<>();

    private UserRepository() {
        initializeUsers();
    }

    private void initializeUsers() {
        User admin = new User(1, "admin", "admin", true);
        users.add(admin);
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    // для тестов
    public static void reset() {
        instance = null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void save(User user) {
        users.add(user);
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public int findLastId() {
        return users.size();
    }


}
