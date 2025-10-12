package com.cultodeportivo.p2_springboot_di.repositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.cultodeportivo.p2_springboot_di.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductRepositoryJson implements ProductRepository{

    private List<Product> list;

    public ProductRepositoryJson() {
        ClassPathResource resource = new ClassPathResource("product.json");
        readValueJson(resource);
    }

    public ProductRepositoryJson(Resource resource) {
        readValueJson(resource);
    }

    private void readValueJson(Resource resource) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = Arrays.asList(
                mapper.readValue(resource.getFile(), Product[].class)
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        return list;
    }

    @Override
    public Product findById(Long id) {
        return list.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
    
}
