package com.cultodeportivo.p5_springboot_interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired()
    @Qualifier("loadingTimeInterceptor")
    private HandlerInterceptor timeInterceptor;

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(timeInterceptor).addPathPatterns("/app/bar"); /app/** -> Todas las rutas */
        registry.addInterceptor(timeInterceptor).excludePathPatterns("/app/foo");
    }

    

}
