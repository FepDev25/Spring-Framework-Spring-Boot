package com.cultodeportivo.p11_spring_security_jwt.services;

import java.util.List;
import java.util.Optional;

import com.cultodeportivo.p11_spring_security_jwt.dto.ProductDTO;
import com.cultodeportivo.p11_spring_security_jwt.entities.Product;

public interface ProductService {

    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(ProductDTO product);
    Optional<Product> update(Long id, ProductDTO product);
    Optional<Product> delete(Long id);
    boolean existsBySku(String sku);

}
