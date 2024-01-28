package org.example.audit;

import java.time.LocalDateTime;

/**
 * Класс действий пользователя
 */
public class UserAction {
    /**
     * Кто выполнил действия
     */
    private String username;
    /**
     * Действие
     */
    private String action;
    /**
     * Время действия
     */
    private LocalDateTime timestamp;

    /**
     * Конструктор, геттеры и сеттеры не нужны
     */
    public UserAction(String username, String action, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    /**
     * Для sout
     */
    @Override
    public String toString() {
        return "UserAction\n" +
                "username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '\n';
    }
}
