package org.example.audit;

import java.util.ArrayList;
import java.util.List;
/**
 * Журнал для хранения всех действий
 * Действия фиксируются только пользователя, посчитал, что администратор не должен здесь светится
 * Также реализовал паттерн Синглтон, потому что используется в разных сервисах
 */
public class AuditLog {
    /**
     * для синглтона
     */
    private static AuditLog instance;
    /**
     * Хранение действий пользователей
     */
    private List<UserAction> userActions;

    /**
     * приватный конструктор для синглтона
     */
    private AuditLog() {
        this.userActions = new ArrayList<>();
    }

    /**
     * Для синглтона
     */
    public static AuditLog getInstance() {
        if (instance == null) {
            instance = new AuditLog();
        }
        return instance;
    }

    /**
     * Добавление в журнал
     */
    public void logAction(UserAction userAction) {
        userActions.add(userAction);
    }

    /**
     * Вывод логов (для администратора)
     */
    public List<UserAction> getUserActions() {
        return userActions;
    }
}


