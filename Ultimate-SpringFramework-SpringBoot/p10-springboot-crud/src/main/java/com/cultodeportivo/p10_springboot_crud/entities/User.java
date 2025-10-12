package com.cultodeportivo.p10_springboot_crud.entities;

import java.util.Date;

import com.cultodeportivo.p10_springboot_crud.validations.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IsRequired(message = "La fecha de nacimiento es requerida con anotaciones personalizadas") 
    private String name;

    @IsRequired(message = "{NotBlank.user.lastname}")
    private String lastname;
    
    private String email;
    private Date birthdate;

    public User() {}

    public User(Long id, String name, String lastname, String email, Date birthdate) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
    }

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
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    
}
