package com.cultodeportivo.p2_springboot_di.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cultodeportivo.p2_springboot_di.models.Product;
import com.cultodeportivo.p2_springboot_di.repositories.ProductRepository;

@Service
public class ProductServiceImp implements ProductService {

    private ProductRepository productRepository;

    @Value("${config.tax}")
    private double tax;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(p -> {
            Double newPrice = p.getPrice() + (p.getPrice() * tax);
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
