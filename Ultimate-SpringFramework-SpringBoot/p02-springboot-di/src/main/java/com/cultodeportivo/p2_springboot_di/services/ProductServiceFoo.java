package com.cultodeportivo.p2_springboot_di.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cultodeportivo.p2_springboot_di.models.Product;
import com.cultodeportivo.p2_springboot_di.repositories.ProductRepository;

@Service("serviceFoo")
public class ProductServiceFoo implements ProductService{

    @Autowired
    private Environment env;

    @Autowired
    private ProductRepository productRepository;
 
    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(p -> {
            Double newPrice = p.getPrice() + (p.getPrice() * Double.valueOf(env.getProperty("config.taxfoo")));
            // Product newProduct = new Product(p.getId(), p.getName(), newPrice.longValue());
            Product newProduct = (Product) p.clone();
            newProduct.setPrice(newPrice.longValue());
            return newProduct;
        }).toList();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id);
    }
    
}
