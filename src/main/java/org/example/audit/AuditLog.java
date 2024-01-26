package org.example.audit;

import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private static AuditLog instance;
    private List<UserAction> userActions;

    private AuditLog() {
        this.userActions = new ArrayList<>();
    }

    public static AuditLog getInstance() {
        if (instance == null) {
            instance = new AuditLog();
        }
        return instance;
    }

    public void logAction(UserAction userAction) {
        userActions.add(userAction);
    }

    public List<UserAction> getUserActions() {
        return userActions;
    }
}


