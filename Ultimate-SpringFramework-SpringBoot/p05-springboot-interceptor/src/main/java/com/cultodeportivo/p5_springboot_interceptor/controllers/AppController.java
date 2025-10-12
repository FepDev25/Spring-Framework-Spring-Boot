package com.cultodeportivo.p5_springboot_interceptor.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/foo")
    public Map<String, String> foo() {
        return Map.of("message", "Handler foo del controlador");
    }

    @GetMapping("/bar")
    public Map<String, String> bar() {
        return Map.of("message", "Handler bar del controlador");
    }

    @GetMapping("/baz")
    public Map<String, String> baz() {
        return Map.of("message", "Handler baz del controlador");
    }
    
}
