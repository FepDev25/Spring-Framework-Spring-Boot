package com.spring.security.jdbc_authentication.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador de vistas
@Controller
public class PageController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public PageController(JdbcUserDetailsManager userDetailsManager,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "admin";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // Procesa el formulario de registro
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        
        // Verificar que el username no este en uso
        if (userDetailsManager.userExists(username)) {
            model.addAttribute("error", "El nombre de usuario ya esta en uso.");
            return "register";
        }

        // Construir un UserDetails con el password hasheado
        UserDetails nuevoUsuario = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities("ROLE_USER")
                .build();

        // Persistir con createUser()
        userDetailsManager.createUser(nuevoUsuario);

        // Redirigir al login con el parametro ?registered para mostrar confirmacion
        return "redirect:/login?registered";
    }
}
