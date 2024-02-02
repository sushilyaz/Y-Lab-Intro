package org.example.service;

import org.example.repository.UserActionRepository;
import org.example.model.UserAction;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Обработчик методов пользователя, также аудит на каждом действии
 */
public class UserService {
    private UserRepository userRepository;
    private UserActionRepository userActionRepository;
    private static int id = 1;

    /**
     * Во время инстанса класса также инициализируется аудит и репозиторий пользователя
     */
    public UserService() {
        this.userActionRepository = UserActionRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Обработчик регистрации пользователя
     */
    public User registerUser(String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            return null;
        } else {
            // поработать с dto (убрать id)
            User newUser = new User(id, username, password, Role.SIMPLE_USER);
            userRepository.save(newUser);
            UserAction userAction = new UserAction(username, "registred", LocalDateTime.now());
            userActionRepository.save(userAction);
            return newUser;
        }
    }
    /**
     * Обработчик аутентификации пользователя. Возвращает "текущего пользователя"
     */
    public User authenticationUser (String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(password)) {
                UserAction userAction = new UserAction(username, "Authentificate success", LocalDateTime.now());
                userActionRepository.save(userAction);
                return user;
            } else {
                UserAction userAction = new UserAction(username, "Authentificate failed. Incorrect password", LocalDateTime.now());
                userActionRepository.save(userAction);
                return null;
            }
        } else {
            UserAction userAction = new UserAction(username, "Authentificate failed. Username not found", LocalDateTime.now());
            userActionRepository.save(userAction);
            return null;
        }
    }
    /**
     * Обработчик закрытия сессии
     */
    public User logoutUser(User user) {
        UserAction userAction = new UserAction(user.getUsername(), "Log out user", LocalDateTime.now());
        userActionRepository.save(userAction);
        return null;
    }
}
