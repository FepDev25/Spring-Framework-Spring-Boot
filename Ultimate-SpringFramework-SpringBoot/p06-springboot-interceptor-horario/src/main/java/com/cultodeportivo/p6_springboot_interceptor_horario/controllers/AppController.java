package com.cultodeportivo.p6_springboot_interceptor_horario.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class AppController {
    
    @GetMapping("/foo")
    public ResponseEntity<?> foo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Bienvenido a la API de Culto Deportivo");
        map.put("time", new Date());
        map.put("message", request.getAttribute("message"));

        return ResponseEntity.ok(map);
    }
    
}
