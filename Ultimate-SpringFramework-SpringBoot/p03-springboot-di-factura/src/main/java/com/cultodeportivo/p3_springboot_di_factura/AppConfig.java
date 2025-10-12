package com.cultodeportivo.p3_springboot_di_factura;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.cultodeportivo.p3_springboot_di_factura.models.Item;
import com.cultodeportivo.p3_springboot_di_factura.models.Product;

@Configuration
@PropertySource(value = "classpath:data.properties", encoding = "UTF-8")
public class AppConfig {

    @Bean
    List<Item> itemsInvoice (){
        Product product1 = new Product("Arroba de arroz",   15);
        Product product2 = new Product("Arroba de maiz", 6.5);
        Product product3 = new Product("Arroba de trigo", 5);
        Product product4 = new Product("Arroba de lentejas", 4);

        Item item1 = new Item(product1, 1);
        Item item2 = new Item(product2, 2);
        Item item3 = new Item(product3, 1);
        Item item4 = new Item(product4, 1);

        return List.of(item1, item2, item3, item4);
    }

    @Bean
    @Primary
    List<Item> itemsInvoiceMinimarket (){
        Product product1 = new Product("Doritos Rojos Med",   0.65);
        Product product2 = new Product("Coca Cola 3L", 3);
        Product product3 = new Product("Polito Chocolate", 0.25);
        Product product4 = new Product("Crema y Gigante", 0.6);
        Product product5 = new Product("Ruffles Crema y Cebolla Med", 0.65);

        Item item1 = new Item(product1, 3);
        Item item2 = new Item(product2, 2);
        Item item3 = new Item(product3, 3);
        Item item4 = new Item(product4, 1);
        Item item5 = new Item(product5, 3);

        return List.of(item1, item2, item3, item4, item5);
    }
    
}
