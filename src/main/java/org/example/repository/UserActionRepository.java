package org.example.repository;

import org.example.model.UserAction;

import java.util.List;

public interface UserActionRepository {
    void save(UserAction userAction);
    List<UserAction> getUserActions();
}
