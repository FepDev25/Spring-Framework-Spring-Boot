package com.cultodeportivo.p10_springboot_crud.entities;

import com.cultodeportivo.p10_springboot_crud.validations.IsExistsDb;
import com.cultodeportivo.p10_springboot_crud.validations.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{NotEmpty.product.name}") // Mensaje personalizado de archivo .properties
    @Size(min = 3, max = 45)
    private String name;

    @Min(0)
    @NotNull
    private double price;

    @NotBlank
    private String description;

    @IsExistsDb 
    @IsRequired
    private String sku;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
}
