package com.cultodeportivo.p2_springboot_di.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p2_springboot_di.models.Product;
import com.cultodeportivo.p2_springboot_di.services.ProductService;


@RestController
@RequestMapping("/api")
public class SomeController {

    private final ProductService service;

    public SomeController(@Qualifier("productServiceImp") ProductService service) {
        this.service = service;
    }
    
    // public SomeController(@Qualifier("serviceFoo") ProductService service) {
    //    this.service = service;
    // }
    
    
    @GetMapping("/list")
    public List<Product> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Product show(@PathVariable long id) {
        return service.findById(id);
    }
   
}
