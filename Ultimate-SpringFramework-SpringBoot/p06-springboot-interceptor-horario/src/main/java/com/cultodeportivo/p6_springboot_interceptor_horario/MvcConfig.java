package com.cultodeportivo.p6_springboot_interceptor_horario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource(value = "classpath:values.properties", encoding="UTF-8")
public class MvcConfig implements WebMvcConfigurer {
    
    @Autowired
    @Qualifier("calendarInterceptor")
    private HandlerInterceptor calendar;

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(calendar).addPathPatterns("/api/foo");
    }
    
}
