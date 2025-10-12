package com.cultodeportivo.p11_spring_security_jwt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p11_spring_security_jwt.dto.ProductDTO;
import com.cultodeportivo.p11_spring_security_jwt.entities.Product;
import com.cultodeportivo.p11_spring_security_jwt.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Product save(ProductDTO product) {

        Product productDb = new Product();
        productDb.setName(product.getName());
        productDb.setPrice(product.getPrice());
        productDb.setDescription(product.getDescription());
        productDb.setSku(product.getSku());

        return repository.save(productDb);
    }

    @Transactional
    @Override
    public Optional<Product> update(Long id, ProductDTO productDto) {

        Optional<Product> productoOp = repository.findById(id);

        if (productoOp.isPresent()) {
            Product productDb = productoOp.orElseThrow();
            productDb.setName(productDto.getName());
            productDb.setPrice(productDto.getPrice());
            productDb.setDescription(productDto.getDescription());
            productDb.setSku(productDto.getSku());
            return Optional.of(repository.save(productDb));
        }

        return productoOp;
    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productoOp = repository.findById(id);
        productoOp.ifPresent(productDb -> repository.delete(productDb));
        return productoOp;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }


    

}
