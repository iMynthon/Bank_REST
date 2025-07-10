package com.example.bankcards.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggableService {

    @Around("@within(com.example.bankcards.aop.LogService)")
    public Object loggingServiceClass(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getDeclaringClass().getSimpleName() + " - " + signature.getName();
        log.info("before service {}, args=[{}]", methodName, joinPoint.getArgs());
        StopWatch time = new StopWatch();
        time.start();
        Object result = joinPoint.proceed();
        time.stop();
        String stopWatchOutput = String.format("method %s executed in %f seconds", methodName,
                time.getTotalTimeSeconds());
        log.info("Execution metrics service: {}", stopWatchOutput);
        log.info("After service: - {}",result == null ? "void" : result);
        return result;
    }
}
