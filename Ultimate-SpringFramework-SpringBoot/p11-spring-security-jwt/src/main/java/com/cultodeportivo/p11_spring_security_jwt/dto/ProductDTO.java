package com.cultodeportivo.p11_spring_security_jwt.dto;

import com.cultodeportivo.p11_spring_security_jwt.validations.IsExistsDb;
import com.cultodeportivo.p11_spring_security_jwt.validations.IsRequired;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    @NotBlank(message = "{NotEmpty.product.name}")
    @Size(min = 3, max = 45)
    private String name;

    @Min(0)
    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @IsExistsDb
    @IsRequired
    private String sku;

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}
