package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс User.
 * Поле isAdmin необходимо для проверки при аутентификации, какую роль назначить
 */
public class User {

    /**
     * id пользователя (необходимо для связки с показанием счетчиков
     * -- GETTER --
     *  Геттеры и сеттеры

     */
    @Getter
    @Setter
    private int id;

    /**
     * Имя пользователя
     */
    @Getter
    @Setter
    private String username;

    /**
     * пароль пользователя
     */
    @Getter
    @Setter
    private String password;

    /**
     * Проверка на админа. True - админ. False - пользователь
     */
    private Role role;

    /**
     * Конструктор
     */
    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getRoleAsString() {
        return role.name(); // Получаем строковое представление enum
    }

    public void setRoleFromString(String role) {
        this.role = Role.valueOf(role); // Преобразуем строку в enum
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
        return role == user.role && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }
}
