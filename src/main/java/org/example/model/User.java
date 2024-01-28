package org.example.model;

import java.util.Objects;

/**
 * Класс User.
 * Поле isAdmin необходимо для проверки при аутентификации, какую роль назначить
 */
public class User {

    /**
     * id пользователя (необходимо для связки с показанием счетчиков
     */
    private int id;

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * пароль пользователя
     */
    private String password;

    /**
     * Проверка на админа. True - админ. False - пользователь
     */
    private boolean isAdmin;

    /**
     * Конструктор
     */
    public User(int id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Геттеры и сеттеры
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "User with " +
                "username = '" + username + '\'' +
                " has: ";
    }

    /**
     * Для тестирования
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, isAdmin);
    }
}
