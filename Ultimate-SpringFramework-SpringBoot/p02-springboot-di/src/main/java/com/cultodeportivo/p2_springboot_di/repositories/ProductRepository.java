package com.cultodeportivo.p2_springboot_di.repositories;

import java.util.List;

import com.cultodeportivo.p2_springboot_di.models.Product;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(Long id);
    
}
