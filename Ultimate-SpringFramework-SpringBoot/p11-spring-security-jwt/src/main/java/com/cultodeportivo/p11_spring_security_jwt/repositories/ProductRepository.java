package com.cultodeportivo.p11_spring_security_jwt.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p11_spring_security_jwt.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    boolean existsBySku(String sku);

}
