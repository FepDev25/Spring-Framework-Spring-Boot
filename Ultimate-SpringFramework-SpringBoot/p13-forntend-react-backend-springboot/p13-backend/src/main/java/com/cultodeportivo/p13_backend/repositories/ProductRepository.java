package com.cultodeportivo.p13_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cultodeportivo.p13_backend.entities.Product;

@CrossOrigin(originPatterns="*")
@RepositoryRestResource(path="products")
public interface  ProductRepository extends CrudRepository<Product, Long> {

}
