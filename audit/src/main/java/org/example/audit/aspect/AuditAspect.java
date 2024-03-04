package org.example.audit.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.audit.model.UserAction;
import org.example.audit.repository.UserActionRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Аспект регистрации действия пользователя
 */

@Aspect
public class AuditAspect {
    private UserActionRepositoryImpl userActionRepository;

    public AuditAspect(UserActionRepositoryImpl userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    @Pointcut("@annotation(org.example.audit.aspect.Audit)")
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
     * Ничего умнее не придумал, пришлось использовать рефлексию, чтобы достать поле username
     * , так как класс user и authDTo не доступы в стартере, они находятся в основном модуле
     * @param joinPoint
     * @return
     */
    private String getUsernameArgument(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            Class<?> clazz = arg.getClass();

            try {
                Field usernameField = clazz.getDeclaredField("username");
                usernameField.setAccessible(true);

                Object usernameValue = usernameField.get(arg);

                if (usernameValue != null) {
                    return usernameValue.toString();
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return "Admin";
    }
}
