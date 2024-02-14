package org.example.model;

import java.util.Objects;

/**
 * Класс User.
 */

public class User {

    private int id;

    private String username;

    private String password;

    private Role role;

    public User() {
    }

    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password) {
        this.id = 1;
        this.username = username;
        this.password = password;
        this.role = Role.SIMPLE_USER;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRoleFromString(String role) {
        this.role = Role.valueOf(role); // Преобразуем строку в enum
    }

    public String getRoleAsString() {
        return role.name(); // Получаем строковое представление enum
    }

    public User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User with " +
                "username = '" + username + '\'' +
                " has: ";
    }

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
