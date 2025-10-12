package com.cultodeportivo.p4_springoot_error.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p4_springoot_error.exceptions.UserNotFoundException;
import com.cultodeportivo.p4_springoot_error.models.domain.User;
import com.cultodeportivo.p4_springoot_error.services.UserService;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private UserService userService;

    @GetMapping("/error1")
    public String index() {

        int a = 1;
        int b = 0;
        int c = a / b;
        System.out.println(c);
        
        return "Hola mundo";
    }

    @GetMapping("/error2")
    public String indexTwo() {
        int c = Integer.parseInt("10a");
        System.out.println(c);
        
        return "Hola mundo";
    }

    @GetMapping("/show/{id}")
    public User show(@PathVariable Long id) {
        return userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}
