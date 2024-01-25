package org.example.service;

import org.example.model.User;
import org.example.repository.CounterReadingRepository;
import org.example.repository.UserRepository;

public class AdminService extends UserService {
    public AdminService(UserRepository userRepository) {
        super(userRepository);
    }

    public User getUser(String username) {
        return getUserRepository().findByUsername(username).get();
    }

}
