package com.cultodeportivo.p7_springboot_aop.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Aspect
@Component
public class GreetingFooAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Before("GreetingServicePointcuts.greetingLoggerPointCut()") // Punto de corte
    public void loggerBefore(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Antes foo: " + method + ", args: " + args);
    }

    @After("GreetingServicePointcuts.greetingLoggerPointCut()") // Punto de corte
    public void loggerAfter(JoinPoint joinPoint) {
        
        String method = joinPoint.getSignature().getName(); // Obtiene el nombre del método
        String args =  Arrays.toString(joinPoint.getArgs()); // Obtiene los argumentos del método

        logger.info("Después foo: " + method + ", args: " + args);
    }
}
