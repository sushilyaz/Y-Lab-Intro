package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.dto.CounterReadingDTO;
import org.example.model.User;
import org.example.model.UserAction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Аспекты по аудиту сервисов.
 */
@Aspect
public class CounterReadingServiceAspect extends BaseAspect{
    // срезы
    @Pointcut("execution(* org.example.service.CounterReadingService.getLastUserInfo(..))")
    public void getLastUserInfo() {
    }
    @Pointcut("execution(* org.example.service.CounterReadingService.getUserInfoForMonth(..))")
    public void getUserInfoForMonth() {
    }
    @Pointcut("execution(* org.example.service.CounterReadingService.getCRByUser(..))")
    public void getCRByUser() {
    }

    @Pointcut("execution(* org.example.service.CounterReadingService.submitCounterReading(..))")
    public void submitCounterReading() {
    }

    /**
     * Ниже аудит каждого сервиса Counter Reading
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "getLastUserInfo()", returning = "result")
    public void lastInfoUserAction(JoinPoint joinPoint, Object result) {
        String username = getUsernameArgument(joinPoint);
        if (result != null) {
            UserAction userAction = new UserAction(username, "Get last counter reading success !", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        } else {
            UserAction userAction = new UserAction(username, "Get last counter reading failed", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        }
    }

    @AfterReturning(pointcut = "getUserInfoForMonth()", returning = "result")
    public void CRInfoForMonth(JoinPoint joinPoint, Object result) {
        String username = getUsernameArgument(joinPoint);
        if (result != null) {
            UserAction userAction = new UserAction(username, "Get counter reading for month success !", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        } else {
            UserAction userAction = new UserAction(username, "Get counter reading for month failed", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        }
    }

    @AfterReturning(pointcut = "getCRByUser()", returning = "result")
    public void AllCRInfoForUser(JoinPoint joinPoint, List<CounterReadingDTO> result) {
        String username = getUsernameArgument(joinPoint);
        if (!result.isEmpty()) {
            UserAction userAction = new UserAction(username, "Get all counter reading for user success !", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        } else {
            UserAction userAction = new UserAction(username, "GGet all counter reading for user failed", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        }
    }

    @AfterReturning(pointcut = "submitCounterReading()", returning = "result")
    public void putData(JoinPoint joinPoint, Object result) {
        String username = getUsernameArgument(joinPoint);
        if (result != null) {
            UserAction userAction = new UserAction(username, "Counter Reading added success !", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        } else {
            UserAction userAction = new UserAction(username, "Counter Reading added failed", LocalDateTime.now());
            userActionRepository.save(userAction);
            System.out.println(userAction);
        }
    }

    /**
     * Метод для получения username для записи в БД
     * @param joinPoint
     * @return
     */
    private String getUsernameArgument(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof User) {
                return ((User) arg).getUsername();
            }
        }
        return null;
    }
}
