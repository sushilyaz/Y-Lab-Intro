package org.example.service;

import org.example.audit.AuditLog;
import org.example.audit.UserAction;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Обработчик методов пользователя, также аудит на каждом действии
 */
public class UserService {
    private UserRepository userRepository;
    private AuditLog auditLog;
    private static int id = 1;

    /**
     * Во время инстанса класса также инициализируется аудит и репозиторий пользователя
     */
    public UserService() {
        this.auditLog = AuditLog.getInstance();
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
            id = userRepository.findLastId() + 1;
            User newUser = new User(id, username, password, false);
            userRepository.save(newUser);
            UserAction userAction = new UserAction(username, "registred", LocalDateTime.now());
            auditLog.logAction(userAction);
            return newUser;
        }
    }
    /**
     * Обработчик аутентицикации пользователя. Возвращает "текущего пользователя"
     */
    public User authenticationUser (String username, String password) {
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isPresent()) {
            User user = existUser.get();
            if (user.getPassword().equals(password)) {
                UserAction userAction = new UserAction(username, "Authentificate success", LocalDateTime.now());
                auditLog.logAction(userAction);
                return user;
            } else {
                UserAction userAction = new UserAction(username, "Authentificate failed. Incorrect password", LocalDateTime.now());
                auditLog.logAction(userAction);
                return null;
            }
        } else {
            UserAction userAction = new UserAction(username, "Authentificate failed. Username not found", LocalDateTime.now());
            auditLog.logAction(userAction);
            return null;
        }
    }
    /**
     * Обработчик закрытия сессии
     */
    public User logoutUser(User user) {
        UserAction userAction = new UserAction(user.getUsername(), "Log out user", LocalDateTime.now());
        auditLog.logAction(userAction);
        return null;
    }
}
