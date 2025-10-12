package com.cultodeportivo.p4_springoot_error.models.domain;

public class User {

    private Long id;
    private String name;
    private String lastName;
    private Role role;

    public User() {
    }

    public User(Long id, String name, String lastName, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}   
