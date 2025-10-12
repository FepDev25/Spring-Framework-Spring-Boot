package com.cultodeportivo.p1_springboot_web.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cultodeportivo.p1_springboot_web.models.User;


@Controller // Indica que es un controlador de Spring
public class UserController {

    @GetMapping("/details")
    public String details (Model model) { 
        // También se puede hacer con Map<String, Object> model y model.put("title", "Hola mundo desde Spring Boot"); 
        User user = new User("Felipe", "Peralta", null);

        model.addAttribute("user", user);
        model.addAttribute("title", "Hola mundo desde Spring Boot");

        return "details";
    }

    @GetMapping("/list")
    public String list(ModelMap model) {
        model.addAttribute("title", "Listado de usuarios");
        // model.addAttribute("users", users); // Con @ModelAttribute ya se puede utilizar 
        return "list";
    }

    @ModelAttribute("users")
    public List<User> usersModel(){
        return List.of(
            new User("Felipe", "Peralta", "felipe@gmail.com"),
            new User("Carlos", "Peralta", null),
            new User("Diego", "Peralta", "diego@gmail.com"),
            new User("Juanita", "Peralta", ""),
            new User("Luis", "Peralta", "luis@gmail.com")
        );
    }
    
}
