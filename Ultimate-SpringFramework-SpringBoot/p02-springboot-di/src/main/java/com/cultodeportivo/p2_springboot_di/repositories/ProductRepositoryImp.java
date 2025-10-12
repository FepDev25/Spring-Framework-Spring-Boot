package com.cultodeportivo.p2_springboot_di.repositories;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cultodeportivo.p2_springboot_di.models.Product;

@Repository
public class ProductRepositoryImp implements ProductRepository {

    private final List<Product> data ;

    public ProductRepositoryImp() {
        data = Arrays.asList(
            new Product(1L, "Samsung S20", 1000L),
            new Product(2L, "Iphone X", 800L),
            new Product(3L, "Huawei P30", 750L),
            new Product(4L, "Xiaomi Mi9", 600L),
            new Product(5L, "Nokia 8", 500L)
        );
    }

    @Override
    public List<Product> findAll() {
        return data;
    }

    @Override
    public Product findById(Long id) {
        return data.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

}
