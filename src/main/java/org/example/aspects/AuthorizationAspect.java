package org.example.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.exception.UnauthorizedException;
import org.example.model.User;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class AuthorizationAspect {

    private UserUtils userUtils;
    @Autowired
    public AuthorizationAspect(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    /**
     * Чтобы не загромождать контроллер, реализовал через аспекты авторизацию пользователя
     */
    @Before("within(org.example.in.controllers.CounterReadingController)")
    public void authorize() {
        User currentUser = userUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("User not authorized");
        }
    }
    /**
     * Проверка на администратора (Разграничение доступа)
     */
    @Before("within(org.example.in.controllers.AdminController)")
    public void isAdmin() {
        User currentUser = userUtils.getCurrentUser();
        if (currentUser == null || !currentUser.getRoleAsString().equals("ADMIN")) {
            throw new UnauthorizedException("Admin not authorized");
        }
    }
}
