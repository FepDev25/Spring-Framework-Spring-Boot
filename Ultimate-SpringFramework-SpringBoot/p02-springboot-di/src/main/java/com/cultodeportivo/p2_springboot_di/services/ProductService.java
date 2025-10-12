package com.cultodeportivo.p2_springboot_di.services;

import java.util.List;

import com.cultodeportivo.p2_springboot_di.models.Product;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
}
