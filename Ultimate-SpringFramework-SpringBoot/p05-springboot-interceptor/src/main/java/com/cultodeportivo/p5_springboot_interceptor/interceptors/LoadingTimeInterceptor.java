package com.cultodeportivo.p5_springboot_interceptor.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("loadingTimeInterceptor")
public class LoadingTimeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoadingTimeInterceptor.class);

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod controller = (HandlerMethod) handler;
        logger.info("LoadingTimeInterceptor: preHandle() entrando: " + controller.getMethod().getName());

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        /* 
        if (controller.getMethod().getName().equals("foo")) {

            Map<String, String> result = new HashMap<>();
            result.put("message", "No tiene autorización");
            result.put("fecha", new Date().toString());

            ObjectMapper mapper = new ObjectMapper();
            String resultString = mapper.writeValueAsString(result);

            response.setContentType("application/json");
            response.setStatus(401);
            response.getWriter().write(resultString);

            System.out.println("Respuesta: " + resultString);

            return false;
        }*/

        return true;
    }

    @SuppressWarnings("null")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        
        HandlerMethod controller = (HandlerMethod) handler;

        long endTime = System.currentTimeMillis();
        long startTime = (Long) request.getAttribute("startTime");
        long executionTime = endTime - startTime;
        
        logger.info("Tiempo de ejecución: " + executionTime + " ms");
        logger.info("LoadingTimeInterceptor: postHandle() saliendo: " + controller.getMethod().getName());
        
    }

    

    
    
}
