package org.example.repository;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс репозитория пользователя. Данные хранятся в листе. Реализован паттерн синглтон, конечно могут возникнуть
 * при множественном доступе, я знаю, как это фиксить, но не стал здесь реализовывать. Если скажете - реализую.
 */
public class UserRepository {
    private static UserRepository instance;
    private List<User> users = new ArrayList<>();

    private UserRepository() {
        initializeUsers();
    }

    /**
     * При инициализации класса также добавляется "Администратор в бд"
     */
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
        return users;
    }

    /**
     * Сохранение пользователя в бд (при регистрации)
     */
    public void save(User user) {
        users.add(user);
    }

    /**
     * Поиск по username
     * @return Optional<User>
     */
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    /**
     * Поиск последнего индекса (для регистрации нового пользователя)
     */
    public int findLastId() {
        return users.size();
    }


}
