package org.example.audit.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс действий пользователя
 */
@Getter
@Setter
public class UserAction {
    private Long id;
    private String username;
    private String action;
    private LocalDateTime timestamp;
    public UserAction(String username, String action, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "UserAction\n" +
                "username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '\n';
    }
}
