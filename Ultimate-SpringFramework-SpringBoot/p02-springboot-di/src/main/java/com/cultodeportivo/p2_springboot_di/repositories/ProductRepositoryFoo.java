package com.cultodeportivo.p2_springboot_di.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cultodeportivo.p2_springboot_di.models.Product;

@Repository
public class ProductRepositoryFoo implements ProductRepository {

    @Override
    public List<Product> findAll() {
        return Collections.singletonList(new Product(1L, "Vehiculo", 4500L));
    }

    @Override
    public Product findById(Long id) {
        return new Product(1L, "Vehiculo", 4500L);
    }
    
}
