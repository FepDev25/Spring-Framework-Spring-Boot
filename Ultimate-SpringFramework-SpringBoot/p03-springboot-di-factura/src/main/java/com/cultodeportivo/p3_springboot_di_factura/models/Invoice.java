package com.cultodeportivo.p3_springboot_di_factura.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@RequestScope
// @JsonIgnoreProperties({"targetSource", "advisors"})
public class Invoice {

    @Autowired
    private Client client;

    @Value("${invoice.description}")
    private String description;
    
    @Autowired
    private List<Item> items;

    @PostConstruct
    public void init(){
        System.out.println("Creando el componente");
        System.out.println(client.getName());
        System.out.println(description);
        items.forEach(action -> System.out.println(action.getProduct().getName()));
    }

    @PreDestroy
    public void destoy(){
        System.out.println("Destruyendo el componente");
    }


    public Invoice(Client client, String description, List<Item> items) {
        this.client = client;
        this.description = description;
        this.items = items;
    }

    public Invoice() {}

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(Item::getTotal).sum();
    }

}
