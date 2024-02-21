package org.example.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class User {

    private Long id;

    private String username;

    private String password;

    private Role role;



    public void setRoleFromString(String role) {
        this.role = Role.valueOf(role); // Преобразуем строку в enum
    }

    public String getRoleAsString() {
        return role.name(); // Получаем строковое представление enum
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role);
    }
}
