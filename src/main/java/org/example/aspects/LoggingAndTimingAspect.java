package org.example.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAndTimingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAndTimingAspect.class);

    @Around("execution(* *(..)) && within(org.example.service..*)")
    public Object logAndTimeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        LOGGER.info("Метод {} выполнился за {} миллисекунд", joinPoint.getSignature(), executionTime);

        return result;
    }
}
