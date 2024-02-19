package org.example.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.dto.AuthDTO;
import org.example.model.User;
import org.example.model.UserAction;
import org.example.repository.UserActionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Аспект регистрации действия пользователя
 */
@Component
@Aspect
public class AuditAspect {
    private UserActionRepositoryImpl userActionRepository;

    @Autowired
    public AuditAspect(UserActionRepositoryImpl userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    @Pointcut("@annotation(org.example.aspects.Audit)")
    public void auditSign() {
    }

    /**
     * Если возвращаемое значение не пустое или не null - успешное выполнение метода, иначе - неудачное
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "auditSign()", returning = "result")
    public void AuditUserAction(JoinPoint joinPoint, Object result) {
        String username = getUsernameArgument(joinPoint);
        String methodName = joinPoint.getSignature().getName();
        if (result instanceof List) {
            if (((List<?>) result).isEmpty()) {
                UserAction userAction = new UserAction(username, methodName + " called failed !", LocalDateTime.now());
                userActionRepository.save(userAction);
                System.out.println(userAction);
            } else {
                UserAction userAction = new UserAction(username, methodName + " called success !", LocalDateTime.now());
                userActionRepository.save(userAction);
                System.out.println(userAction);
            }
        } else {
            if (result != null) {
                UserAction userAction = new UserAction(username, methodName + " called success !", LocalDateTime.now());
                userActionRepository.save(userAction);
                System.out.println(userAction);
            } else {
                UserAction userAction = new UserAction(username, methodName + " called failed !", LocalDateTime.now());
                userActionRepository.save(userAction);
                System.out.println(userAction);
            }
        }
    }

    /**
     * Получение имени юзера, выполняющий действия
     * @param joinPoint
     * @return
     */
    private String getUsernameArgument(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof User ) {
                return ((User) arg).getUsername();
            } else if (arg instanceof AuthDTO) {
                return ((AuthDTO) arg).getUsername();
            }
        }
        return "Admin";
    }
}