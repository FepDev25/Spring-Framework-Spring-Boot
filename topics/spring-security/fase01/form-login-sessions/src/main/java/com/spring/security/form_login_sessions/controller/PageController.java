package com.spring.security.form_login_sessions.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador de vistas
@Controller
public class PageController {

    // muestra el formulario de login
    // el post del formulario se procesa automaticamente por Spring Security
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Pagina principal post-login. Accesible para cualquier usuario autenticado.
    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "home";
    }

    // Pagina exclusiva para administradores
    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "admin";
    }

    // Redirige la raiz del sitio a /home
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
}
