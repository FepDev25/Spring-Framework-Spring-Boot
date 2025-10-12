package com.cultodeportivo.p1_springboot_web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p1_springboot_web.dto.UserDto;
import com.cultodeportivo.p1_springboot_web.models.User;


@RestController
@RequestMapping("/api")
public class UserRestController {

    @GetMapping("/details")
    public Map<String, Object> details() {
        User user = new User("Felipe", "Peralta", "email@gmail.com");

        Map<String, Object> body = new HashMap<>();

        body.put("title", "Hola mundo desde Spring Boot");
        body.put("user", user);

        return body;
    }

    @GetMapping("/list")
    public List<User> list() {
        User user = new User("Felipe", "Peralta", "email@gmail.com");
        User user2 = new User("Carlos", "Peralta", "email@gmail.com");
        User user3 = new User("Diego", "Peralta", "email@gmail.com");

        return List.of(user, user2, user3);
    }

    @GetMapping("/detailsdto")
    public UserDto detailsDto() {
        
        UserDto userDto = new UserDto();
        userDto.setTitle("Hola mundo desde Spring Boot");
        userDto.setUser(new User("Felipe", "Peralta", "email@gmail.com"));

        return userDto;
    }
    
}
