package org.example.model;


import lombok.Getter;
import lombok.Setter;

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
}
