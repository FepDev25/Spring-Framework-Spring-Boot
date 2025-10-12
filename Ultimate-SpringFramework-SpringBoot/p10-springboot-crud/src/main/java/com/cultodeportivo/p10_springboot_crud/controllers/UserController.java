package com.cultodeportivo.p10_springboot_crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p10_springboot_crud.entities.User;
import com.cultodeportivo.p10_springboot_crud.services.UserService;
import com.cultodeportivo.p10_springboot_crud.validations.UserValidation;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserValidation userValidation;

    @GetMapping()
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> userOp = service.findById(id);
        if (userOp.isPresent()){
            return ResponseEntity.ok(userOp.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        userValidation.validate(user, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        User newUser = service.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result ,@PathVariable Long id) {
        // userValidation.validate(user, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<User> userOp = service.update(id, user);
        if (userOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOp.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOp = service.delete(id);
        if (userOp.isPresent()) {
            return ResponseEntity.ok(userOp.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors =new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + ": " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

}


        
