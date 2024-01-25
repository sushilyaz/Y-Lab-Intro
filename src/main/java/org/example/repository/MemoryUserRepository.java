package org.example.repository;

import org.example.model.User;
import org.example.util.CurrentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryUserRepository implements UserRepository {

    private List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
