package com.cultodeportivo.p10_springboot_crud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p10_springboot_crud.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    boolean existsBySku(String sku);

}
