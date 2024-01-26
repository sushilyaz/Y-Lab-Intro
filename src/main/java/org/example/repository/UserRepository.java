package org.example.repository;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public UserRepository() {
        User admin = new User(1, "admin", "admin", true);
        users.add(admin);
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
