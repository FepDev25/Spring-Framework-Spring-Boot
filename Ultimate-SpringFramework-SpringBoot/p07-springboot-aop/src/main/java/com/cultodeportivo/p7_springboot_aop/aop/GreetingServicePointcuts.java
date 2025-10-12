package com.cultodeportivo.p7_springboot_aop.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class GreetingServicePointcuts {

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")    
    public void greetingLoggerPointCut(){}

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHelloError(..))")    
    public void greetingLoggerPointCutError(){}

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHelloAround(..))")    
    public void greetingLoggerPointCutAround(){}

    
}
