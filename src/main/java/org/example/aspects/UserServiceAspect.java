//package org.example.aspects;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.example.dto.AuthDTO;
//import org.example.dto.UserDTO;
//import org.example.model.UserAction;
//import org.example.repository.UserActionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
///**
// * Аспекты по аудиту сервисов User.
// */
//@Aspect
//@Component
//public class UserServiceAspect {
//    private UserActionRepository userActionRepository;
//
//    @Autowired
//    public UserServiceAspect(UserActionRepository userActionRepository) {
//        this.userActionRepository = userActionRepository;
//    }
//    // срезы
//    @Pointcut("execution(* org.example.service.UserServiceImpl.registerUser(..))")
//    public void userServiceRegistration() {
//    }
//    @Pointcut("execution(* org.example.service.UserServiceImpl.authenticationUser(..))")
//    public void userServiceAuthentification() {
//    }
//    /**
//     * Ниже аудит каждого сервиса User
//     * @param joinPoint
//     * @param result
//     */
//    @AfterReturning(pointcut = "userServiceRegistration()", returning = "result")
//    public void registrationUserAction(JoinPoint joinPoint, Object result) {
//        String username = getUsernameArgument(joinPoint);
//        if (result != null) {
//            UserAction userAction = new UserAction(username, "Registered success !", LocalDateTime.now());
//            userActionRepository.save(userAction);
//            System.out.println(userAction);
//        } else {
//            UserAction userAction = new UserAction(username, "Registered failed.", LocalDateTime.now());
//            userActionRepository.save(userAction);
//            System.out.println(userAction);
//        }
//    }
//
//    @AfterReturning(pointcut = "userServiceAuthentification()", returning = "result")
//    public void authUserAction(JoinPoint joinPoint, Object result) {
//        String username = getUsernameArgument(joinPoint);
//        if (result != null) {
//            UserAction userAction = new UserAction(username, "Authentificated success !", LocalDateTime.now());
//            userActionRepository.save(userAction);
//            System.out.println(userAction);
//        } else {
//            UserAction userAction = new UserAction(username, "Authentificated failed", LocalDateTime.now());
//            userActionRepository.save(userAction);
//            System.out.println(userAction);
//        }
//    }
//    /**
//     * Метод для получения username для записи в БД
//     * @param joinPoint
//     * @return
//     */
//    private String getUsernameArgument(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
//            if (arg instanceof AuthDTO) {
//                return ((AuthDTO) arg).getUsername();
//            } else if (arg instanceof UserDTO) {
//                return ((UserDTO) arg).getUsername();
//            }
//        }
//        return null;
//    }
//}
