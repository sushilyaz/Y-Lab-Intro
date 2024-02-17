package org.example.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.exception.UnauthorizedException;
import org.example.model.User;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    private UserUtils userUtils;
    @Autowired
    public AuthorizationAspect(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @Before("within(org.example.in.servlets.CounterReadingController)")
    public void authorize() {
        User currentUser = userUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("User not authorized");
        }
        // Дополнительные проверки, если необходимо
    }
}
