package com.cultodeportivo.p7_springboot_aop.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p7_springboot_aop.services.GreetingService;


@RestController
public class GreetingController {

    @Autowired
    private GreetingService greetingService;


    @GetMapping("/greeting")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok(
            Map.of("greeting", greetingService.sayHello("Felipe", "Hola cómo vas"))
        );
    }

    @GetMapping("/greeting-error")
    public ResponseEntity<?> greetingError() {
        return ResponseEntity.ok(
            Map.of("greeting", greetingService.sayHelloError("Felipe", "Hola cómo vas"))
        );
    }

    @GetMapping("/greeting-around")
    public ResponseEntity<?> greetingAround() {
        return ResponseEntity.ok(
            Map.of("greeting", greetingService.sayHelloAround("Felipe", "Hola cómo vas"))
        );
    }
    
    
}
