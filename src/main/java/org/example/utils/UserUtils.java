package org.example.utils;

import lombok.Data;
import org.example.model.User;
import org.springframework.stereotype.Component;

/**
 * Для реализации сессии. Понимаю, что в многопоточном случае так работать не будет, но так реализовать было быстрее +
 * можно было сделать через токены, но не успевал
 */
@Component
@Data
public class UserUtils {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
