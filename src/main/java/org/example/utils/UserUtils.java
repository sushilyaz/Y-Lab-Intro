package org.example.utils;

import lombok.Data;
import org.example.model.User;
import org.springframework.stereotype.Component;


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
