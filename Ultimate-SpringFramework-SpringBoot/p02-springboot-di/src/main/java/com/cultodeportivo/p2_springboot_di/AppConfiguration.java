package com.cultodeportivo.p2_springboot_di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import com.cultodeportivo.p2_springboot_di.repositories.ProductRepository;
import com.cultodeportivo.p2_springboot_di.repositories.ProductRepositoryJson;

@Configuration
@PropertySource(value="classpath:values.properties", encoding = "UTF-8")
public class AppConfiguration {

    @Value("classpath:product.json")
    private Resource resource;

    @Bean
    @Primary
    @SuppressWarnings("unused")
    ProductRepository productRepositoryJson() {
        return new ProductRepositoryJson(resource);
    }
}
