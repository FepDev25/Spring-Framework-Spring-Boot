package com.cultodeportivo.p12_client_app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p12_client_app.models.Message;


@RestController
public class AppController {

    @GetMapping("/list")
    public List<Message> get(){
        return List.of(
            new Message("Chelsea FC campeon!"),
            new Message("Java el mejor lenguaje"),
            new Message("One piece el mejor anime"),
            new Message("Rick and Morty la mejor serie")
        );
    }

    @PostMapping("/create") 
    public Message create(@RequestBody Message message){
        System.out.println("Mensaje guardado: " + message.getMessage());
        return message;
    }  

    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam String code){
        return Map.of(
            "code", code
        );
    }
}
