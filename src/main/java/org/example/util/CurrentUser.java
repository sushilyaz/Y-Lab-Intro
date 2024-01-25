package org.example.util;

import org.example.model.User;

public class CurrentUser {
    private User currentUser;

    public CurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
