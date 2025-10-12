package com.cultodeportivo.p3_springboot_di_factura.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
// @JsonIgnoreProperties({"targetSource", "advisors"})
public class Client {

    @Value("${client.name}")
    private String name;    

    @Value("${client.lastName}")
    private String lastName;

    public Client(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Client() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    
    
}
