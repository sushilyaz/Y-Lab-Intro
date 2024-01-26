package org.example.audit;

import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private List<UserAction> userActions;

    public AuditLog() {
        this.userActions = new ArrayList<>();
    }

    public void logAction(UserAction userAction) {
        userActions.add(userAction);
    }

    public List<UserAction> getUserActions() {
        return userActions;
    }
}

