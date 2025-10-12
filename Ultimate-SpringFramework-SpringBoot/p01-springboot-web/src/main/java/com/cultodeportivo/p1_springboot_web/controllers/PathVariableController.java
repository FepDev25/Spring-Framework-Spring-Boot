package com.cultodeportivo.p1_springboot_web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p1_springboot_web.dto.ParamDto;
import com.cultodeportivo.p1_springboot_web.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/path")
public class PathVariableController {

    @Value("${config.code}")
    private Integer code;

    @Value("${config.username}")
    private String username;

    @Value("${config.message}")
    private String message;

    @Value("${config.listOfValues}")
    private String[] listOfValues; // Puede ser String[] o List<String>

    @Value("#{'${config.listOfValues}'.split(',')}")
    private List<String> listOfValues2;

    @Value("#{'${config.listOfValues}'}")
    private String listString;

    @Value("#{${config.valuesMap}}")
    private Map<String, Object> valuesMap;

    @Value("#{${config.valuesMap}.product}")
    private String product;

    @Value("#{${config.valuesMap}.price}")
    private String price;

    @Autowired
    private Environment env;

    @GetMapping("/baz/{message}")
    public ParamDto baz(@PathVariable String message) {

        ParamDto paramDto = new ParamDto();
        paramDto.setMessage(message);
        
        return paramDto;
    }

    @GetMapping("/baz-multiple/{product}/{id}")
    public Map<String, Object> bazMultiple(@PathVariable String product, @PathVariable Long id) {

        Map <String, Object> body = new HashMap<>();
        body.put("product", product);
        body.put("id", id);
        
        return body;
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {

        StringBuilder sbEmail = new StringBuilder(user.getEmail());
        sbEmail.reverse();
        
        User entity = new User();
        entity.setName(user.getName().toUpperCase());
        entity.setLastname(user.getLastname().toLowerCase());
        entity.setEmail(sbEmail.toString());
        
        return entity;
    }

    @GetMapping("/values")
    public Map<String, Object> values() {

        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("username", username);
        body.put("message", message);
        body.put("listOfValues", listOfValues);
        body.put("listOfValues2", listOfValues2);
        body.put("listString", listString);
        body.put("valuesMap", valuesMap);
        body.put("product", product);
        body.put("price", price);

        return body;
    }

    @GetMapping("/values-param")
    public Map<String, Object> valuesParam(@Value("${config.esposa}") String esposa) {

        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("username", username);
        body.put("message", message);
        body.put("esposa", esposa);
        body.put("listOfValues", listOfValues);

        return body;
    }

    @SuppressWarnings("null")
    @GetMapping("/environment")
    public Map<String, Object> environment() throws JsonMappingException, JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("code", Integer.valueOf(env.getProperty("config.code"))); // Hacer cast 1
        body.put("age", env.getProperty("config.age", Integer.class)); // Hacer cast 2
        body.put("username", env.getProperty("config.username"));
        body.put("message", env.getProperty("config.message"));
        body.put("listOfValues", env.getProperty("config.listOfValues").split(",") ); // Hacer cast 1
        body.put("listOfValues2", env.getProperty("config.listOfValues", String[].class)); // Hacer cast 2
        
        return body;
    }
    
    
    

}
