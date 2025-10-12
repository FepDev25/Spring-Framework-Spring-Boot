package com.cultodeportivo.p11_spring_security_jwt.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @SuppressWarnings("null") 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
}
