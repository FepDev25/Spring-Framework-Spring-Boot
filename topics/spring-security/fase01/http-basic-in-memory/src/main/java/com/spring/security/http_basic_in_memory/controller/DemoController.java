package com.spring.security.http_basic_in_memory.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador que expone tres tipos de endpoints para demostrar los
// niveles de acceso configurados en SecurityConfig
@RestController
public class DemoController {

    // GET /public/hello: sin autenticacion
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Endpoint publico: no se requiere autenticacion.";
    }

    // GET /hello: cualquier usuario autenticado
    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hola, " + authentication.getName()
                + ". Roles asignados: " + authentication.getAuthorities();
    }

    // GET /admin/info: solo rol ADMIN
    @GetMapping("/admin/info")
    public String adminInfo(Authentication authentication) {
        return "Panel de administracion. Usuario: " + authentication.getName()
                + " | Authorities: " + authentication.getAuthorities();
    }
}
