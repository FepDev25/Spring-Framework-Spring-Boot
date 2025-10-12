package com.cultodeportivo.p7_springboot_aop.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Order(1)
public class GreetingAspect {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Before("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")
    @Before("GreetingServicePointcuts.greetingLoggerPointCut()") // Punto de corte
    public void loggerBefore(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Antes: " + method + ", args: " + args);
    }

    @After("GreetingServicePointcuts.greetingLoggerPointCut()") // Punto de corte
    public void loggerAfter(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Después: " + method + ", args: " + args);
    }


    @AfterReturning("GreetingServicePointcuts.greetingLoggerPointCut()") // Punto de corte
    public void loggerAfterReturning(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Después de retornar: " + method + ", args: " + args);
    }

    @AfterThrowing("GreetingServicePointcuts.greetingLoggerPointCutError()") // Punto de corte
    public void loggerAfterThrowing(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Después de lanzar una excepción: " + method + ", args: " + args);
    }

    @Around("GreetingServicePointcuts.greetingLoggerPointCutAround()")
    public Object loggerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        try {
            logger.info("ANTES: El método: " + method + "() con los parámetros: " + args);

            Object result = joinPoint.proceed();

            logger.info("DESPUÉS: El método: " + method + "() retorna: " +  result);

            return result;
        } catch (Throwable e) {
            logger.info("ERROR: Error en la llamada del método: " + method + "(), error: " +  e.getMessage());
            throw e;
        }
    } 
}
